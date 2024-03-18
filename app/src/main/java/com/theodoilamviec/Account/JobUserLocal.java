package com.theodoilamviec.Account;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "JobUserLocal")
public class JobUserLocal {
     @PrimaryKey(autoGenerate = true)
     public Long id ;
     public String idJob ;
     public String idJobUser ;
     public String idUser ;
     public String nameUser ;
     public String idProject ;

     public JobUserLocal(String idJob, String idJobUser, String idUser, String nameUser, String idProject) {
          this.idJob = idJob;
          this.idJobUser = idJobUser;
          this.idUser = idUser;
          this.nameUser = nameUser;
          this.idProject = idProject;
     }
}
