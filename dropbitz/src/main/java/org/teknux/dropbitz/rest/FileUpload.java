package org.teknux.dropbitz.rest;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

@Path("upload")
public class FileUpload {

	@POST
	@Path("file")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(
            @FormDataParam("file") final InputStream fileInputStream,
            @FormDataParam("file") final FormDataContentDisposition contentDispositionHeader) {
        try {
        	java.nio.file.Path outputPath = FileSystems.getDefault().getPath("/home/pp/Desktop", "TOTO-" + contentDispositionHeader.getFileName());
            Files.copy(fileInputStream, outputPath);
        } catch (IOException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
        }

        return Response.status(Response.Status.OK.getStatusCode()).build();
    }
}
