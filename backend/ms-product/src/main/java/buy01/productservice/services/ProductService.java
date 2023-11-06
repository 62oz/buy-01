package buy01.productservice.services;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import buy01.productservice.config.JwtService;
import buy01.productservice.enums.Role;
import buy01.productservice.exceptions.ResourceNotFoundException;
import buy01.productservice.models.Product;
import buy01.productservice.models.ProductResponse;
import buy01.productservice.repositories.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    private JwtService jwtService;

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductResponse> productResponses = products.stream()
                                                        .map(this::mapToProductResponse)
                                                        .collect(Collectors.toList());
        return productResponses;
    }

    public ProductResponse getProductById(String id) {
        Product product = productRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Product not found for this id :: " + id));
        return mapToProductResponse(product);
    }

    public List<ProductResponse> getProductsByUserId(String userId) {
        List<Product> products =  productRepository.findByUserId(userId);
        List<ProductResponse> productResponses = products.stream()
                                                        .map(this::mapToProductResponse)
                                                        .collect(Collectors.toList());
        return productResponses;
    }

    public Product createProduct(Product product, String authenticatedUserName) throws AccessDeniedException {
        String token = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        String userId = jwtService.extractUserId(token);
        Role role = Role.valueOf(jwtService.extractRole(token));

        boolean isSeller = role.equals(Role.ROLE_SELLER);
        if (!isSeller) {
            throw new AccessDeniedException("You must be a seller to create a product.");
        }

        product.setUserId(userId);

        return productRepository.save(product);
    }

    public Product updateProduct(String id, Product updatedProduct, String authenticatedUserName) throws AccessDeniedException {
        Product product = productRepository.findById(id)
                                    .orElseThrow(() -> new ResourceNotFoundException("Product not found for this id :: " + id));

        String token = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        String userId = jwtService.extractUserId(token);
        Role role = Role.valueOf(jwtService.extractRole(token));

        boolean isSeller = role.equals(Role.ROLE_SELLER);
        if (!isSeller) {
            throw new AccessDeniedException("You must be a seller to update a product.");
        }

        boolean isAdmin = role.equals(Role.ROLE_ADMIN);
        boolean isOwner = productRepository.findById(id).get().getUserId().equals(userId);

        if (isAdmin || isOwner) {
            updatedProduct.setId(id);
            updatedProduct.setUserId(product.getUserId());
            if (updatedProduct.getName() == null) {
                updatedProduct.setName(product.getName());
            }
            if (updatedProduct.getDescription() == null) {
                updatedProduct.setDescription(product.getDescription());
            }
            return productRepository.save(updatedProduct);
        } else {
            throw new AccessDeniedException("Access denied");
        }
    }

    public void deleteProduct(String id, String authenticatedUserName) throws AccessDeniedException {
        String token = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        String userId = jwtService.extractUserId(token);
        Role role = Role.valueOf(jwtService.extractRole(token));

        boolean isSeller = role.equals(Role.ROLE_SELLER);
        if (!isSeller) {
            throw new AccessDeniedException("You must be a seller to delete a product.");
        }

        Product product = productRepository.findById(id)
                                            .orElseThrow(() -> new ResourceNotFoundException("Product not found for this id :: " + id));
        String productId = product.getUserId();

        boolean isAdmin = role.equals(Role.ROLE_ADMIN);
        boolean isOwner = false;
        if (productId != null && userId != null) {
            isOwner = productId.equals(userId);
        }

        if (isAdmin || isOwner) {
            productRepository.deleteById(id);
        } else {
            throw new AccessDeniedException("Access denied");
        }
    }

    private ProductResponse mapToProductResponse(Product product) {
        String token = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        String userId = jwtService.extractUserId(token);
        Role role = Role.valueOf(jwtService.extractRole(token));


        ProductResponse.ProductResponseBuilder responseBuilder = ProductResponse.builder()
                                                                                 .id("hidden")
                                                                                 .name(product.getName())
                                                                                 .description(product.getDescription())
                                                                                 .price(product.getPrice())
                                                                                 .userId("hidden");

        if (token != null) {
            boolean isAdmin = role.equals(Role.ROLE_ADMIN);
            boolean isOwner = userId.equals(product.getUserId());

            if (isAdmin || isOwner) {
                responseBuilder.id(product.getId())
                               .userId(product.getUserId());
            }
        }

        return responseBuilder.build();
    }
}
