import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

import java.io.*;

public class S3Uploader {

    private static String bucketName ;
    private static String folderName ;
    private static String fileNameInS3;
    private static String fileNameInLocalPC;

    public S3Uploader(String bucketName, String folderName, String fileNameInS3, String fileNameInLocalPC){
        S3Uploader.bucketName = bucketName;
        S3Uploader.folderName = folderName;
        S3Uploader.fileNameInS3 = fileNameInS3;
        S3Uploader.fileNameInLocalPC = fileNameInLocalPC;
    }

    public void start()
    {
        try {
            boolean isUploaded = false;
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withRegion("ap-south-1").withCredentials(new AWSStaticCredentialsProvider(AWSAuth.connect()))
                    .build();
            while(!isUploaded){
                if (s3Client.doesBucketExistV2(bucketName)) {
                    uploadFile(bucketName, s3Client);
                    isUploaded =true;
                } else {
                    createBucket(s3Client);
                }
            }
        } catch (AmazonS3Exception e) {
            e.printStackTrace();
        }
    }

    private void createBucket(AmazonS3 s3Client){
        try {
            s3Client.createBucket(bucketName);
        } catch (AmazonS3Exception e) {
            System.err.println(e.getErrorMessage());
        }
    }

    private void uploadFile(String bucketName, AmazonS3 s3Client){
        try {
            System.out.format("Bucket %s already exists.\n", bucketName);
            PutObjectRequest request = new PutObjectRequest(bucketName, folderName + "/" + fileNameInS3, new File(fileNameInLocalPC));
            s3Client.putObject(request);
            System.out.println("--Uploading file done");
            S3Object fullObject;
            fullObject = s3Client.getObject(new GetObjectRequest(bucketName, folderName + "/" + fileNameInS3));
            System.out.println("--Downloading file done");
            InputStream is = fullObject.getObjectContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            System.out.println("--File content:");
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }}
