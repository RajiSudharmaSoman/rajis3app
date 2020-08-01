package assgn1.s3app;

import software.amazon.awssdk.regions.Region;

public final class Constants {
	public static final Region REGION = Region.US_EAST_1;
	public static final String S3_FILE_PREFIX = "s3test";
	public static final String S3_FILE_SUFFIX = ".tmp";
	public static final String S3_BUCKET_NAME = "raji-a1-s3bucket";
	public static final String S3_TMP_KEY_NAME = "raji-detail.tmp";
	public static final String S3_TXT_KEY_NAME = "in-app.txt";
}
