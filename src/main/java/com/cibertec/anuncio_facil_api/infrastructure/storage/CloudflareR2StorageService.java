package com.cibertec.anuncio_facil_api.infrastructure.storage;

import com.cibertec.anuncio_facil_api.application.service.MediaStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.net.URI;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CloudflareR2StorageService implements MediaStorageService {

    @Value("${storage.endpoint}")
    private String endpoint;

    @Value("${storage.bucket}")
    private String bucket;

    @Value("${storage.access-key}")
    private String accessKey;

    @Value("${storage.secret-key}")
    private String secretKey;

    @Value("${storage.region:auto}")
    private String region;

    @Value("${storage.public-base-url:}")
    private String publicBaseUrl;

    @Override
    public String upload(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("Archivo vacío");
        }

        String key = buildKey(file.getOriginalFilename());
        try (S3Client client = buildClient()) {
            PutObjectRequest putRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .contentType(detectContentType(file))
                    .acl(ObjectCannedACL.PUBLIC_READ)
                    .build();

            client.putObject(putRequest, RequestBody.fromBytes(file.getBytes()));
        } catch (Exception e) {
            throw new RuntimeException("Error subiendo archivo a R2", e);
        }

        return buildPublicUrl(key);
    }

    private S3Client buildClient() {
        return S3Client.builder()
                .endpointOverride(URI.create(endpoint))
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)
                ))
                .build();
    }

    private String buildKey(String originalFilename) {
        String cleanName = originalFilename == null ? "file" : originalFilename.replaceAll("\\s+", "_");
        return "uploads/" + Instant.now().toEpochMilli() + "-" + UUID.randomUUID() + "-" + cleanName;
    }

    private String detectContentType(MultipartFile file) {
        String type = file.getContentType();
        return type != null ? type : MediaType.APPLICATION_OCTET_STREAM_VALUE;
    }

    private String buildPublicUrl(String key) {
        String base = (publicBaseUrl != null && !publicBaseUrl.isBlank()) ? publicBaseUrl : endpoint + "/" + bucket;
        if (base.endsWith("/")) {
            base = base.substring(0, base.length() - 1);
        }
        return base + "/" + key;
    }
}
