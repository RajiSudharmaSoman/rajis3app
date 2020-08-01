package assgn1.s3app;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Utilities;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

/**
 * Amazon S3 assignment
 *
 */
public class App 
{
	private static S3Client s3Client;

	public static void main(String[] args) throws IOException {

		s3Client = S3Client.builder().credentialsProvider(ProfileCredentialsProvider.create()).region(Constants.REGION).build();
		uploadToS3();
		readfromS3();
	}
	//Upload Temp File to s3
	public static void uploadToS3() throws IOException {
		String bucketName=Constants.S3_BUCKET_NAME;
		String keyName=Constants.S3_TMP_KEY_NAME;
		File tempFile = File.createTempFile(Constants.S3_FILE_PREFIX,Constants.S3_FILE_SUFFIX);

		List<String> content = Arrays.asList("Full Name:Raji", "Student Id:55555", "No.of Units Taken:4");
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))) {
			for (String s : content) {
				bw.write(s);
				bw.write(System.lineSeparator());
			}
		}

		RequestBody requestBody = RequestBody.fromFile(tempFile);

		try {

			PutObjectRequest putObjectRequest = PutObjectRequest.builder().bucket(bucketName).key(keyName)
					.build();
			s3Client.putObject(putObjectRequest, requestBody);
			S3Utilities utilities = S3Utilities.builder().region(Constants.REGION).build();
			GetUrlRequest request = GetUrlRequest.builder().bucket(bucketName).key(keyName).build();
			URL url = utilities.getUrl(request);
			System.out.println("Object URL is "+url.toExternalForm());
			
		} finally {
			tempFile.delete();
		}
	}
	//Read temp fle from s3 and writing the contents to txt file
	public static void readfromS3() throws IOException {
		String bucketName=Constants.S3_BUCKET_NAME;
		String keyName=Constants.S3_TMP_KEY_NAME;
		try {
			GetObjectRequest getObjectRequest = GetObjectRequest.builder().bucket(bucketName).key(keyName)
					.build();
			
			ResponseInputStream<GetObjectResponse> s3object = s3Client.getObject(getObjectRequest);
			 
			FileOutputStream fos = new FileOutputStream(new File(Constants.S3_TXT_KEY_NAME));
			byte[] read_buf = new byte[1024];
			int read_len = 0;

			while ((read_len = s3object.read(read_buf)) > 0) {
				fos.write(read_buf, 0, read_len);
			}
			s3object.close();
			fos.close();
		} catch (IOException e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
	}

}
