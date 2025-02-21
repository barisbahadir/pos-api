package com.bahadir.pos.utils;

import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

public class ImageUtils {

    public static String compressImage(String base64Image) {
        String image;
        float quality = 0.8F;

        try {
            if (base64Image == null || StringUtils.isBlank(base64Image)) {
                return base64Image;
            }

            // 1. Base64 verisini byte[]'a çevir
            byte[] imageBytes = Base64.getDecoder().decode(base64Image);
            ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes);

            // 2. BufferedImage'ye dönüştür
            BufferedImage originalImage = ImageIO.read(bais);

            // 3. JPEG formatında bir dosya kaydedici oluştur
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageWriter writer = ImageIO.getImageWritersByFormatName("jpeg").next();
            ImageWriteParam param = writer.getDefaultWriteParam();

            // 4. Kaliteyi belirleyin (0.0 - 1.0 arasında, 1.0 en yüksek kalite)
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(quality);

            // 5. Resmi yaz
            writer.setOutput(ImageIO.createImageOutputStream(baos));
            writer.write(null, new javax.imageio.IIOImage(originalImage, null, null), param);
            baos.flush();

            byte[] compressedImageBytes = baos.toByteArray();
            baos.close();

            // 6. Base64 formatına dönüştür
            image = Base64.getEncoder().encodeToString(compressedImageBytes);
        } catch (Exception e) {
            image = base64Image;
        }

        return image;
    }

}
