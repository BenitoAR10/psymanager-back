package bo.com.ucb.psymanager.service;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.RemoveObjectArgs;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

/**
 * Servicio encargado de gestionar la subida y consulta de archivos en MinIO,
 * como los audios de ejercicios de bienestar emocional.
 */
@Service
@Slf4j
public class MinioService {

    private final MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucketName;

    @Value("${minio.region}")
    private String region;

    public MinioService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    /**
     * Sube un archivo de audio al bucket configurado en MinIO.
     *
     * @param file Archivo de audio (por ejemplo, MP3 o WAV)
     * @param objectName Nombre con el que se almacenará el archivo (debe ser único o con prefijo controlado)
     */
    public void uploadExerciseAudio(MultipartFile file, String objectName) {
        try (InputStream inputStream = file.getInputStream()) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(inputStream, file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
        } catch (Exception e) {
            log.error("Error al subir archivo a MinIO", e);
            throw new RuntimeException("No se pudo subir el archivo a MinIO", e);
        }
    }

    /**
     * Genera una URL temporal para acceder a un archivo almacenado en MinIO.
     *
     * NOTA: En producción móvil se recomienda usar `MINIO_PUBLIC_HOST + objectName`
     * para asegurar compatibilidad con dispositivos que no acceden a localhost.
     *
     * @param objectName Nombre del objeto que se desea acceder
     * @return URL válida por 1 hora
     */
    public String getPresignedUrl(String objectName) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .method(Method.GET)
                            .expiry(1, TimeUnit.HOURS)
                            .build()
            );
        } catch (Exception e) {
            log.error("Error al generar URL firmada de MinIO", e);
            throw new RuntimeException("No se pudo generar la URL firmada", e);
        }
    }

    /**
     * Elimina un archivo del bucket.
     *
     * @param objectName Nombre del objeto a eliminar
     */
    public void deleteObject(String objectName) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
        } catch (Exception e) {
            log.error("Error al eliminar archivo de MinIO", e);
            throw new RuntimeException("No se pudo eliminar el archivo de MinIO", e);
        }
    }
}