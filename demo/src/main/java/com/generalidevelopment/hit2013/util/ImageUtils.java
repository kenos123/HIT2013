package com.generalidevelopment.hit2013.util;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ImageUtils {

	protected final static Log logger = LogFactory.getLog(ImageUtils.class);

	public static byte[] scaleJpg(final byte[] fileData, int width, int height) {
		final ByteArrayInputStream in = new ByteArrayInputStream(fileData);
		try {
			final BufferedImage img = ImageIO.read(in);
			if (height == 0) {
				height = (width * img.getHeight()) / img.getWidth();
			}
			if (width == 0) {
				width = (height * img.getWidth()) / img.getHeight();
			}
			final Image scaledImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
			final BufferedImage imageBuff = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			imageBuff.getGraphics().drawImage(scaledImage, 0, 0, new Color(0, 0, 0), null);

			final ByteArrayOutputStream buffer = new ByteArrayOutputStream();

			ImageIO.write(imageBuff, "jpg", buffer);

			return buffer.toByteArray();
		} catch (final IOException e) {
			logger.error("Image scale exception", e);
		}
		return fileData;
	}
}
