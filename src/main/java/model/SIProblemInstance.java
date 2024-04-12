package model;

import helpers.ImageHelper;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class SIProblemInstance {

    public static final String ROOT_RES_PATH = "/training_data";

    public final int InstanceId;

    public final BufferedImage TestImage;

    public SIProblemInstance(int instanceId) throws IOException {
        this.InstanceId = instanceId;
        this.TestImage = LoadImage("Test image.jpg");
    }

    private BufferedImage LoadImage(String fileName) throws IOException {
        return ImageHelper.LoadImage(ROOT_RES_PATH + "/" + InstanceId + "/" + fileName);
    }

}
