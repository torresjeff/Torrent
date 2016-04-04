/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author manuela
 */
public class ManejadorArchivos
{
    public static void DividirArchivo(String nombre, int partes)
    {
        File inputFile = new File(nombre);
        FileInputStream inputStream;
        String newFileName;
        FileOutputStream filePart;
        int fileSize = (int)inputFile.length();
        int nChunks = 0, read = 0;
        int readLength = (int)fileSize/partes;
        byte[] byteChunkPart;
        
        try
        {
            inputStream = new FileInputStream(inputFile);
            while (fileSize > 0)
            {
                if (fileSize <= readLength)
                {
                    readLength = fileSize;
                }
                byteChunkPart = new byte[readLength];
                read = inputStream.read(byteChunkPart, 0, readLength);
                fileSize -= read;
                assert (read == byteChunkPart.length);
                nChunks++;
                newFileName = nombre + ".part"
                        + Integer.toString(nChunks - 1);
                filePart = new FileOutputStream(new File(newFileName));
                filePart.write(byteChunkPart);
                filePart.flush();
                filePart.close();
                byteChunkPart = null;
                filePart = null;
            }
            inputStream.close();
        }
        catch (IOException exception)
        {
            exception.printStackTrace();
        }
        
        
        
    }
    
    public static void UnirArchivo(String nombre, int partes)
    {
        File ofile = new File(nombre);
        FileOutputStream fos;
        FileInputStream fis;
        byte[] fileBytes;
        int bytesRead = 0;
        List<File> list = new ArrayList<File>();
        for (int i = 0; i < partes; ++i)
        {
            list.add(new File(nombre+".part"+i));
        }
        
        try {
            fos = new FileOutputStream(ofile,true);
            for (File file : list) {
                fis = new FileInputStream(file);
                fileBytes = new byte[(int) file.length()];
                bytesRead = fis.read(fileBytes, 0,(int)  file.length());
                assert(bytesRead == fileBytes.length);
                assert(bytesRead == (int) file.length());
                fos.write(fileBytes);
                fos.flush();
                fileBytes = null;
                fis.close();
                fis = null;
                
                Files.deleteIfExists(FileSystems.getDefault().getPath("compartidos", file.getName()));
            }
            fos.close();
            fos = null;
        }catch (Exception exception){
            exception.printStackTrace();
        }
    }
    
    public static String GenerarHash(String nombre)
    {
        StringBuffer sb = new StringBuffer();
        
        FileInputStream fis = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            fis = new FileInputStream(nombre);
            byte[] dataBytes = new byte[1024];
            int nread = 0;
            while ((nread = fis.read(dataBytes)) != -1)
            {
                md.update(dataBytes, 0, nread);
            }
            
            byte[] mdbytes = md.digest();
            
            for (int i = 0; i < mdbytes.length; i++) {
                sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
            }  
            System.out.println("Hash para " + nombre + ": " + sb.toString());
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ManejadorArchivos.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ManejadorArchivos.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(ManejadorArchivos.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fis.close();
            } catch (IOException ex) {
                Logger.getLogger(ManejadorArchivos.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return sb.toString();
    }
}
