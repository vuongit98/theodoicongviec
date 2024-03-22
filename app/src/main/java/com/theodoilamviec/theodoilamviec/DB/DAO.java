package com.theodoilamviec.theodoilamviec.DB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.theodoilamviec.Account.JobDocument;
import com.theodoilamviec.Account.JobDocumentLocal;
import com.theodoilamviec.Account.JobLocal;
import com.theodoilamviec.Account.JobNotificationLocal;
import com.theodoilamviec.Account.JobUserLocal;
import com.theodoilamviec.Account.PermissionJobLocal;
import com.theodoilamviec.ProjectLocal;
import com.theodoilamviec.theodoilamviec.models.Category;
import com.theodoilamviec.theodoilamviec.models.Note;
import com.theodoilamviec.theodoilamviec.models.Notification;
import com.theodoilamviec.theodoilamviec.models.TrashNote;

import java.util.List;

@Dao
public interface DAO {

    /*
        Note DAO
     */

    // request search notes
    @Query("SELECT * FROM notes ORDER BY CASE WHEN :sort_by = 'note_id' THEN note_id END DESC," +
            "CASE WHEN :sort_by = 'a_z' THEN note_title END ASC," +
            "CASE WHEN :sort_by = 'z_a' THEN note_title END DESC")
    List<Note> request_notes(String sort_by);

    // request search notes by global
    @Query("SELECT * FROM notes WHERE note_title LIKE '%' || :keyword || '%'" +
            "OR note_description LIKE '%' || :keyword || '%' ORDER BY note_id DESC")
    List<Note> request_search_notes_by_global(String keyword);

    // request search notes by color and note theme
    @Query("SELECT * FROM notes WHERE (note_title LIKE '%' || :keyword || '%'" +
            "OR note_description LIKE '%' || :keyword || '%') AND note_color = :color ORDER BY note_id DESC")
    List<Note> request_search_notes_by_color(String keyword, String color);

    // request search notes by images
    @Query("SELECT * FROM notes WHERE (note_title LIKE '%' || :keyword || '%'" +
            "OR note_description LIKE '%' || :keyword || '%') AND note_image_path != '' ORDER BY note_id DESC")
    List<Note> request_search_notes_by_images(String keyword);

    // request search notes by videos
    @Query("SELECT * FROM notes WHERE (note_title LIKE '%' || :keyword || '%'" +
            "OR note_description LIKE '%' || :keyword || '%') AND note_video_path != '' ORDER BY note_id DESC")
    List<Note> request_search_notes_by_video(String keyword);

    // request search notes by reminders
    @Query("SELECT * FROM notes WHERE (note_title LIKE '%' || :keyword || '%'" +
            "OR note_description LIKE '%' || :keyword || '%') AND note_reminder != '' ORDER BY note_id DESC")
    List<Note> request_search_notes_by_reminder(String keyword);

    // request search notes by category
    @Query("SELECT * FROM notes WHERE note_category_id = :identifier ORDER BY note_id DESC")
    List<Note> request_notes_by_category(int identifier);

    // request reminder notes
    @Query("SELECT * FROM notes WHERE note_reminder != '' ORDER BY note_id DESC")
    List<Note> request_reminder_notes();

    // request remove lock
    @Query("UPDATE notes SET note_locked = 0")
    int request_remove_lock();

    // request insert a new note
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void request_insert_note(Note note);

    @Delete
    void request_delete_note(Note note);

    /*
        Archive DAO
     */


    /*
        Category DAO
     */

    @Query("SELECT * FROM categories ORDER BY category_id DESC")
    List<Category> request_categories();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void request_insert_category(Category category);

    @Delete
    void request_delete_category(Category category);

    /*
        Notification DAO
     */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void requestInsertNotification(Notification notification);

    @Query("DELETE FROM notification WHERE id = :id")
    void requestDeleteNotification(long id);

    @Query("DELETE FROM notification")
    void requestDeleteAllNotification();

    @Query("SELECT * FROM notification")
    List<Notification> requestAllNotifications();

    @Query("SELECT * FROM notification WHERE id = :id LIMIT 1")
    Notification requestNotification(long id);


    @Query("SELECT COUNT(id) FROM notification")
    Integer requestNotificationCount();

    /*
        Trash DAO
     */

    @Query("SELECT * FROM trash_notes ORDER BY note_id DESC")
    List<TrashNote> request_trash_notes();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void request_insert_trash_note(TrashNote trash_note);

    @Delete
    void request_delete_trash_note(TrashNote trash_note);

    @Query("DELETE FROM trash_notes")
    void request_delete_all_trash_note();
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertJobNotificationLocal(JobNotificationLocal jobNotification);

    @Query("UPDATE JobNotificationLocal SET timeEnd = :timeEnd WHERE idJob = :idJob AND idProject = :idProject")
    void updateJobNotificationLocal(String idJob, String idProject, Long timeEnd);

    @Query("DELETE FROM JobNotificationLocal WHERE idJob = :idJob AND idProject = :idProject")
    void deleteJobNotificationLocal(String idJob, String idProject);


    @Query("SELECT * FROM JobNotificationLocal ")
    List<JobNotificationLocal> requestAllJobNotificationLocal();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertProjects(ProjectLocal projectLocal);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertJobs(JobLocal jobLocal);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertJobNotifications(JobNotificationLocal jobNotificationLocal);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertJobDocument(JobDocumentLocal jobDocument);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertJobUser(JobUserLocal jobUserLocal);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPermissionJob(PermissionJobLocal permissionJobLocal);

    @Query("DELETE FROM JobNotificationLocal WHERE idProject = :idProject AND idJob = :idJob")
    void deleteJobNotificationLocalById(String idProject, String idJob);
}