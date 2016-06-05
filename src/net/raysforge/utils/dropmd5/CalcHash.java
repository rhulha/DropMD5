package net.raysforge.utils.dropmd5;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ArrayBlockingQueue;

import net.raysforge.easyswing.EasyTable;

public class CalcHash extends Thread {
	
	ArrayBlockingQueue<Integer> abq = new ArrayBlockingQueue<Integer>(100);
	private final EasyTable easyTable;
	
	public CalcHash(EasyTable easyTable) {
		this.easyTable = easyTable;
		setDaemon(true);
	}
	
	public static String toHex(byte[] bytes) {
	    BigInteger bi = new BigInteger(1, bytes);
	    return String.format("%0" + (bytes.length << 1) + "X", bi);
	}

	
	@Override
	public void run() {
		try {
			while( true)
			{
				int row = abq.take();
				//System.out.println("row: " + row);
				String filename = easyTable.getValue( row, 0);
				File f = new File( filename);
				FileInputStream fis = new FileInputStream(f);
				BufferedInputStream bis = new BufferedInputStream(fis, 20000000);
				MessageDigest md = MessageDigest.getInstance("MD5");
				byte[] buf = new byte[2048000];
				long count = f.length();
				while( true)
				{
					int read = bis.read(buf);
					if( read < 0)
						break;
					count-=read;
					easyTable.setValue(""+count, row, 1);
					md.update(buf, 0, read);
				}
				byte[] digest = md.digest();
				String md5hex = new BigInteger(1, digest).toString(16);
				easyTable.setValue(md5hex, row, 1);
				bis.close();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void add( int row)
	{
		abq.add(row);
	}

}
