package model;

import utils.ImageUtils;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class SIProblemInstance {

    public static final String ROOT_RES_PATH = "/training_data";

    public final int InstanceId;

    public final BufferedImage TestImage;

    public final int MaxSegmentCount;

    public SIProblemInstance(int instanceId, int maxSegmentCount) throws IOException {
        this.InstanceId = instanceId;
        this.MaxSegmentCount = maxSegmentCount;
        this.TestImage = LoadImage("Test image.jpg");
    }

    private BufferedImage LoadImage(String fileName) throws IOException {
        return ImageUtils.LoadImage(ROOT_RES_PATH + "/" + InstanceId + "/" + fileName);
    }

}
