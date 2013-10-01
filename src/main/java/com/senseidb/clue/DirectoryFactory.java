package com.senseidb.clue;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class DirectoryFactory {

  public DirectoryFactory() {
  }
  
  public Directory buildDirectory(String idxPath) throws IOException {
    if (idxPath.contains("://")) {
      int idx = idxPath.indexOf("://");
      String protocol = idxPath.substring(0, idx);      
      String path = idxPath.substring(idx+"://".length());
      
      if ("file".equals(protocol)) {
        return FSDirectory.open(new File(path));
      }
      else if ("hdfs".equals(protocol)){        
        Path hdfsPath = new Path(idxPath);
        return new HdfsDirectory(path, hdfsPath.getFileSystem(new Configuration()));
      }
      else {
        throw new IOException("unsupported protocol: "+protocol);
      }
    }
    else {
      return FSDirectory.open(new File(idxPath));
    }
  }

}
