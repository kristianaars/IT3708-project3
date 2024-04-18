package model;

import utils.ImageUtils;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class SIProblemInstance {

    public static final String ROOT_RES_PATH = "/training_data";

    public final int InstanceId;

    public final BufferedImage TestImage;

    public final int SegmentCount;

    public SIProblemInstance(int instanceId, int segmentCount) throws IOException {
        this.InstanceId = instanceId;
        this.SegmentCount = segmentCount;
        this.TestImage = LoadImage("Test image.jpg");
    }

    private BufferedImage LoadImage(String fileName) throws IOException {
        return ImageUtils.LoadImage(ROOT_RES_PATH + "/" + InstanceId + "/" + fileName);
    }

}
