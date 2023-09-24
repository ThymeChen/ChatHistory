package cn.thymechen.xiaoming.history.utility;

import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.utils.ExternalResource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Base64;

public class ImageUtil {
    public static String toBase64(Image image) throws IOException {
        try (final InputStream inputStream = new URL(Image.queryUrl(image)).openStream()) {
            return "[base64," + Base64.getEncoder().encodeToString(inputStream.readAllBytes()) + "]";
        }
    }

    public static Image toImage(String base64, Contact contact) throws IOException {
        try (final InputStream inputStream = new ByteArrayInputStream(Base64.getDecoder().decode(base64))) {
            return ExternalResource.uploadAsImage(inputStream, contact);
        }
    }
}
