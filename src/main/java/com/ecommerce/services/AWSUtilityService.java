package com.ecommerce.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.GroupGrantee;
import com.amazonaws.services.s3.model.Permission;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.ecommerce.S3Config;

@Service
public class AWSUtilityService {
	
	@Autowired
	private S3Config s3Config;
	
	private AmazonS3 s3client;
	
	public void setupS3Client() {
		if(s3client == null) {
			AWSCredentials credentials = new BasicAWSCredentials(s3Config.getAccessKey(), s3Config.getSecretkey());
			s3client = AmazonS3ClientBuilder
					  .standard()
					  .withCredentials(new AWSStaticCredentialsProvider(credentials))
					  .withRegion(Regions.AP_SOUTH_1)
					  .build();
		}
	}
	
	
	private File convertMultiPartToFile(MultipartFile file) throws IOException {
		File convFile = new File(file.getOriginalFilename());
	    FileOutputStream fos = new FileOutputStream(convFile);
	    fos.write(file.getBytes());
	    fos.close();
	    return convFile;
	}

	
	public String uploadFilesToS3(MultipartFile file) throws IOException  {
		setupS3Client();
		if(!s3client.doesBucketExist(s3Config.getBucket())) {
			s3client.createBucket(s3Config.getBucket());
		}
		
		File imageFile = null;
		try {
			imageFile = convertMultiPartToFile(file);
			PutObjectRequest putObjectRequest = new PutObjectRequest(s3Config.getBucket(), 
					file.getOriginalFilename(), imageFile);
			 AccessControlList acl = new AccessControlList();
			 acl.grantPermission(GroupGrantee.AllUsers, Permission.Read); //all users or authenticated
			 putObjectRequest.setAccessControlList(acl);
			 s3client.putObject(putObjectRequest);
		} catch (IOException e) {
			throw e;
		} finally {
			if(imageFile!=null) {
				imageFile.delete();
			}
		}
		
		
		return s3Config.getEndpointUrl() + "/" + file.getOriginalFilename();
	}
	
	public void deleteFileInS3(String fileName) {
		setupS3Client();
		s3client.deleteObject(s3Config.getBucket(),fileName);
	}

}
