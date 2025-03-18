/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Archive;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import java.awt.Color;
import java.awt.Image;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author RIIO
 */
public class DASHBOARD extends javax.swing.JFrame {

    /**
     * Creates new form DASHBOARD
     */
  private static final String APPLICATION_NAME = "RIIO APP";
  private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
  private static DefaultTableModel model;
  private static DefaultListModel modelTitles,modelSetTitles;
  private static DefaultComboBoxModel<String> modelSubFolders;
  private static final String CREDENTIALS_FILE_PATH_DRIVE = "auth/credentials_auth.json";
  private static String names,department,useremail,usercode,title,folderId,docType="",docDesc="",regOn="";
 java.io.File selectedFile,selectedFileAll;
  List<List<Object>> filesInfo;
  private static List<String>SubFoldersId,owners,ownersCode,typeUser,loadedFiles,loadedNames;
    public DASHBOARD() {
        super("RIIO-APP / DASHBOARD");
        initComponents();
        lbUser.setText("STAFF:"+names);
        lbEmail.setText("EMAIL:"+useremail);
        lbDep.setText("DEPARTMENT:"+department);
    }
    public DASHBOARD(String username,String email,String userCode,String dep,String titles,String folder) {
        super("RIIO-APP / DASHBOARD");
      initComponents();
      names=username;
      department=dep;
      useremail=email;
      title=titles;
      usercode=userCode;
      folderId=folder;
      lbUser.setText("USER:"+names);
      lbEmail.setText("EMAIL:"+useremail);
      lbDep.setText("DEPARTMENT:"+department);
      lbTitle.setText("TITLE:"+title);  
     if(!title.equals("Manager") && !title.equals("Director") && !title.equals("Administrator"))
      {
          btnOtherArchive1.setVisible(false);
          btnCreateFolderArchive.setVisible(false);
          //btnOtherArchive1.setLayout(null);
          
      }
     resizeImage();
     workPlace.removeAll();
     workPlace.add(pnDashboard);
     workPlace.setTitleAt(0, "HOME");
    }
private void resizeImage()
{
    try{
   ImageIcon originalIcon = new ImageIcon(getClass().getResource("photo/hospital pic.jpeg"));
   ImageIcon originalLogo = new ImageIcon(getClass().getResource("photo/simple.jpg"));
            // Resize the ImageIcon
        // Set the new height
            Image resizedImage = originalIcon.getImage().getScaledInstance(lbimage.getWidth(),
                    lbimage.getHeight(), Image.SCALE_SMOOTH);
            ImageIcon resizedIcon = new ImageIcon(resizedImage);
            lbimage.setIcon(resizedIcon);
            Image resizedLogo = originalLogo.getImage().getScaledInstance(lblogo.getWidth(),
                    lblogo.getHeight(), Image.SCALE_SMOOTH);
            ImageIcon resizedIconLogo = new ImageIcon(resizedLogo);
            lblogo.setIcon(resizedIconLogo);
     
           
    }
    catch(Exception e)
    {
     JOptionPane.showMessageDialog(null, e);
    }
            
}
public Set titles(String dep) throws IOException, GeneralSecurityException
{
  
      // Build a new authorized API client service.
     
    final String spreadsheetId = "1CHbGWR1L3KLxpSC6aYcAXTnS0cOeikMPq4jziT67RdY";
    final String range = "Basic_Information";
    GoogleCredential credential;
        try (InputStream credentialsStream = DASHBOARD.class.getResourceAsStream(CREDENTIALS_FILE_PATH_DRIVE)) {
            credential = GoogleCredential.fromStream(credentialsStream);
        }
    credential = credential.createScoped(Collections.singleton(SheetsScopes.SPREADSHEETS));
    Sheets service = new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, credential)
      .setApplicationName(APPLICATION_NAME)
      .build();
    ValueRange response = service.spreadsheets().values()
        .get(spreadsheetId, range)
        .execute();
    Set<String>titles=new HashSet();
    List<List<Object>> values = response.getValues();
    if (values == null || values.isEmpty()) {
      JOptionPane.showMessageDialog(null,"No data found.");
    } else {
        
      for (List row : values)
      {
        // Print columns A and E, which correspond to indices 0 and 4.
        if(row.get(5).equals(dep))
        {
         titles.add(String.valueOf(row.get(4)));
        }
      
      }
    }
     
    return titles;
}
        public static String generateRandomPassword(int length) {
        // Define the characters allowed in the password
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        // Create a StringBuilder to store the password
        StringBuilder password = new StringBuilder();

        // Create a Random object
        Random random = new Random();

        // Generate random characters until the password length is reached
        for (int i = 0; i < length; i++) {
            // Generate a random index within the range of the characters string
            int index = random.nextInt(characters.length());

            // Append the character at the random index to the password
            password.append(characters.charAt(index));
        }

        // Convert StringBuilder to String and return the password
        return password.toString();
    }
    public void downloadFile(int row,String whois)
    {
         GoogleCredentials credentials = null;
    try (InputStream credentialsStream = DASHBOARD.class.getResourceAsStream(CREDENTIALS_FILE_PATH_DRIVE)) {
            credentials = GoogleCredentials.fromStream(credentialsStream);
        } catch (IOException ex) {
         JOptionPane.showMessageDialog(null, ex);
      }
//LOGIN.class.getResourceAsStream(CREDENTIALS_FILE_PATH)
    credentials = credentials.createScoped(Arrays.asList(DriveScopes.DRIVE_FILE));
    HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(
        credentials);
    // Build a new authorized API client service.
    Drive service = new Drive.Builder(new NetHttpTransport(),
        GsonFactory.getDefaultInstance(),
        requestInitializer)
        .setApplicationName(APPLICATION_NAME)
        .build();
        OutputStream outputStream;
      try {
          JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Choose where to save the file");
        int userSelection = fileChooser.showSaveDialog(null);
        //fileChooser.setSelectedFile(new File(loadedNames.get(row)+".pdf"));
        String savePath=null;
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            savePath=fileChooser.getSelectedFile().getParent()+"/"+loadedNames.get(row);
        } else {
            throw new IllegalArgumentException("File selection cancelled.");
        }
                    outputStream = new FileOutputStream(savePath);
   service.files().
           get(loadedFiles.get(row))
     .executeMediaAndDownloadTo(outputStream);
     outputStream.flush();
     outputStream.close();
     if(whois.equals("MINE"))
     {
     lbMsg4.setText("File successfully downloaded");
     lbMsg4.setForeground(Color.blue);
     }
     if(whois.equals("ANY"))
     {
     lbMsg5.setText("File successfully downloaded");
     lbMsg5.setForeground(Color.blue);
     }
      } catch (IOException  ex) {
          JOptionPane.showMessageDialog(null, ex);
      }
    }
    public static String createFolder(String folderName,String typeUser) throws IOException {
    // Load pre-authorized user credentials from the environment.
    // TODO(developer) - See https://developers.google.com/identity for
    // guides on implementing OAuth2 for your application.
    GoogleCredentials credentials;
    try (InputStream credentialsStream = DASHBOARD.class.getResourceAsStream(CREDENTIALS_FILE_PATH_DRIVE)) {
            credentials = GoogleCredentials.fromStream(credentialsStream);
        }
//LOGIN.class.getResourceAsStream(CREDENTIALS_FILE_PATH)
    credentials = credentials.createScoped(Arrays.asList(DriveScopes.DRIVE_FILE));
    HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(
        credentials);

    // Build a new authorized API client service.
    Drive service = new Drive.Builder(new NetHttpTransport(),
        GsonFactory.getDefaultInstance(),
        requestInitializer)
        .setApplicationName(APPLICATION_NAME)
        .build();
    // File's metadata.
    File fileMetadata = new File();
    fileMetadata.setName(folderName);
    fileMetadata.setMimeType("application/vnd.google-apps.folder");
    if(typeUser.equals("Administration"))
    {
    fileMetadata.setParents(Collections.singletonList("1F4i3S5iSRp55xiRwilMux_07-qonOh4T"));
    }
    if(typeUser.equals("User"))
    {
    fileMetadata.setParents(Collections.singletonList("1vBhrfeof4YDKCrKB5DuITujQCfuRv3jw"));
    }
    try {
      File file = service.files().create(fileMetadata)
          .setFields("id,name")
          .execute();
      
      return file.getId();
    } catch (GoogleJsonResponseException e) {
      // TODO(developer) - handle error appropriately
      JOptionPane.showMessageDialog(null, "Unable to create folder: " + e.getDetails());
      throw e;
    }
  }
    public void uploadFiletoFolder(String ownerId,String fileType,String filename,String filedesc,String userType)
    {
        try {
                Date d=new Date();
                SimpleDateFormat dte = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");
                filename=filename.replace(" ", "_");
                filename=filename+"_"+txOwner.getText()+"_"+dte.format(d);
                String id=uploadFile(filename,selectedFileAll,ownerId);
                final String spreadsheetId = "1CHbGWR1L3KLxpSC6aYcAXTnS0cOeikMPq4jziT67RdY";
    final String range = "Files_Archive_Info";
    GoogleCredential credential;
        try (InputStream credentialsStream = DASHBOARD.class.getResourceAsStream(CREDENTIALS_FILE_PATH_DRIVE)) {
            credential = GoogleCredential.fromStream(credentialsStream);
        }
    credential = credential.createScoped(Collections.singleton(SheetsScopes.SPREADSHEETS));
    Sheets service = new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, credential)
      .setApplicationName(APPLICATION_NAME)
      .build();
    ValueRange response = service.spreadsheets().values()
        .get(spreadsheetId, range)
        .execute();
    List<List<Object>> values = response.getValues();
    if (values == null || values.isEmpty()) {
      JOptionPane.showMessageDialog(null,"No data found.");
    }
    else
    {
        dte = new SimpleDateFormat("dd-MM-yyyy");
        int ro=values.size();
        ValueRange body = new ValueRange()
            .setValues(Arrays.asList(Arrays.asList(ownersCode.get(mnSubFolders.getSelectedIndex()-1),txOwner.getText(),userType,filename,fileType,id,dte.format(d),filedesc)));
            // Write the data to the specified range in the spreadsheet
            service.spreadsheets().values()
            .update(spreadsheetId,"Files_Archive_Info!A"+(ro+1)+":H"+(ro+1), body)
            .setValueInputOption("RAW")
            .execute();
    }
                lbMsg1.setText("File uploaded successfully");
                lbMsg1.setForeground(Color.blue);
            } catch (IOException | GeneralSecurityException ex) {
                JOptionPane.showMessageDialog(null, ex);
            }
    }
    public static Boolean loadFolders(String parentFolderId,String user,String dep) throws IOException, GeneralSecurityException
    {
     List<String> fileNames = new ArrayList<>();
    GoogleCredentials credentials;
        try (InputStream credentialsStream = DASHBOARD.class.getResourceAsStream(CREDENTIALS_FILE_PATH_DRIVE)) {
            credentials = GoogleCredentials.fromStream(credentialsStream);
        }
//LOGIN.class.getResourceAsStream(CREDENTIALS_FILE_PATH)
    credentials = credentials.createScoped(Arrays.asList(DriveScopes.DRIVE_FILE));
    HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(
        credentials);
    // Build a new authorized API client service.
    Drive service = new Drive.Builder(new NetHttpTransport(),
        GsonFactory.getDefaultInstance(),
        requestInitializer)
        .setApplicationName(APPLICATION_NAME)
        .build();
    // File's metadata.
    //File fileMetadata = new File();
    //fileMetadata.setName();
    //fileMetadata.setMimeType("application/vnd.google-apps.file");
    //fileMetadata.setParents(Collections.singletonList(folderId));
    try {    
        List<File> folders = service.files().list()
                    .setQ("'" + parentFolderId + "' in parents and mimeType = 'application/vnd.google-apps.folder'")
                    .setPageSize(1000)
                    .setFields("files(id, name)")
                    .execute()
                    .getFiles();
                
            // Output the folder names and IDs
            if (folders != null && !folders.isEmpty()) 
            {
                owners=new ArrayList<>();
                ownersCode=new ArrayList<>();
                typeUser=new ArrayList<>();
                modelSubFolders= new DefaultComboBoxModel<>();
                modelSubFolders.addElement("Select Folder");
                SubFoldersId=new ArrayList<>();
                for (File folder : folders) {
                    if(user.equals("User"))
                    {
    final String spreadsheetId = "1CHbGWR1L3KLxpSC6aYcAXTnS0cOeikMPq4jziT67RdY";
    final String range = "Basic_Information";
    GoogleCredential credential;
        try (InputStream credentialsStream = LOGIN.class.getResourceAsStream(CREDENTIALS_FILE_PATH_DRIVE)) {
            credential = GoogleCredential.fromStream(credentialsStream);
        }
    credential = credential.createScoped(Collections.singleton(SheetsScopes.SPREADSHEETS));
    Sheets serviceSheet = new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, credential)
      .setApplicationName(APPLICATION_NAME)
      .build();
    ValueRange response = serviceSheet.spreadsheets().values()
        .get(spreadsheetId, range)
        .execute();
    List<List<Object>> values = response.getValues();
    if (values == null || values.isEmpty()) {
      JOptionPane.showMessageDialog(null,"No data found.");
    } else {
        
      for (List row : values) {
        // Print columns A and E, which correspond to indices 0 and 4.
        if(row.get(11).equals(folder.getId()) && row.get(5).equals(dep))
        {
if(department.equals(row.get(5)) || title.equals("Director") || title.equals("Administrator"))
{
    if(!title.equals("Manager") && !title.equals("Director") && !title.equals("Administrator") && !(row.get(12).equals("Resident")) )
    {
        continue;
    }
            modelSubFolders.addElement(folder.getName()); 
            SubFoldersId.add(folder.getId());
            owners.add((String)row.get(2));
            ownersCode.add((String)row.get(1));
            typeUser.add((String)row.get(4));
            
}
break;
        }
        }
        }      
        }  
//////////////////////////////////////**********
if(user.equals("Administration"))
                    {
    final String spreadsheetId = "1CHbGWR1L3KLxpSC6aYcAXTnS0cOeikMPq4jziT67RdY";
    final String range = "Administration_Information";
    GoogleCredential credential;
        try (InputStream credentialsStream = DASHBOARD.class.getResourceAsStream(CREDENTIALS_FILE_PATH_DRIVE)) {
            credential = GoogleCredential.fromStream(credentialsStream);
        }
    credential = credential.createScoped(Collections.singleton(SheetsScopes.SPREADSHEETS));
    Sheets serviceSheet = new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, credential)
      .setApplicationName(APPLICATION_NAME)
      .build();
    ValueRange response = serviceSheet.spreadsheets().values()
        .get(spreadsheetId, range)
        .execute();
    List<List<Object>> values = response.getValues();
    if (values == null || values.isEmpty()) {
      JOptionPane.showMessageDialog(null,"No data found.");
    } else {
        
      for (List row : values) {
        // Print columns A and E, which correspond to indices 0 and 4.
        if(row.get(5).equals(folder.getId()))
        {
            
if((department.equals(row.get(7)) && String.valueOf(row.get(6)).contains(title)) || title.equals("Admimistrator") || title.equals("Director"))
               {
            modelSubFolders.addElement(folder.getName()); 
            SubFoldersId.add(folder.getId());
            owners.add((String)row.get(2));
            ownersCode.add((String)row.get(1));
             typeUser.add((String)row.get(7));
               } 
        }
        }
        }      
        }
                }
                return true;
            } else {
                
                return false;
            }
    } catch (GoogleJsonResponseException e) {
      // TODO(developer) - handle error appropriately
      JOptionPane.showMessageDialog(null, "Unable to create file: " + e.getDetails());
      throw e;
    } 
    }
    
    public static Boolean isFileRecorded(String docName,String docId) throws IOException, GeneralSecurityException
    {
    final String spreadsheetId = "1CHbGWR1L3KLxpSC6aYcAXTnS0cOeikMPq4jziT67RdY";
    final String range = "Files_Archive_Info";
    GoogleCredential credential;
        try (InputStream credentialsStream = DASHBOARD.class.getResourceAsStream(CREDENTIALS_FILE_PATH_DRIVE)) {
            credential = GoogleCredential.fromStream(credentialsStream);
        }
    credential = credential.createScoped(Collections.singleton(SheetsScopes.SPREADSHEETS));
    Sheets service = new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, credential)
      .setApplicationName(APPLICATION_NAME)
      .build();
    ValueRange response = service.spreadsheets().values()
        .get(spreadsheetId, range)
        .execute();
    List<List<Object>> values = response.getValues();
    if (values == null || values.isEmpty()) {
      JOptionPane.showMessageDialog(null,"No data found.");
    } else {
        for (List row : values) {
            docType="";
            if(row.get(0).equals(usercode) && row.get(3).equals(docName) && row.get(5).equals(docId))
            {
                docType=(String)row.get(4);
                regOn=(String)row.get(6);
                docDesc=(String)row.get(7);
                return true;
            }
        }
    }
     return false;   
    }
    public static String uploadFile(String fileName,java.io.File file,String folderid) throws IOException {
    // Load pre-authorized user credentials from the environment.
    // TODO(developer) - See https://developers.google.com/identity for
    // guides on implementing OAuth2 for your application.
    GoogleCredentials credentials;
        try (InputStream credentialsStream = DASHBOARD.class.getResourceAsStream(CREDENTIALS_FILE_PATH_DRIVE)) {
            credentials = GoogleCredentials.fromStream(credentialsStream);
        }
//LOGIN.class.getResourceAsStream(CREDENTIALS_FILE_PATH)
    credentials = credentials.createScoped(Arrays.asList(DriveScopes.DRIVE_FILE));
    HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(
        credentials);
    // Build a new authorized API client service.
    Drive service = new Drive.Builder(new NetHttpTransport(),
        GsonFactory.getDefaultInstance(),
        requestInitializer)
        .setApplicationName(APPLICATION_NAME)
        .build();
    // File's metadata.
    
    File fileMetadata = new File();
    fileMetadata.setName(fileName);
    fileMetadata.setMimeType("application/octet-stream");
    fileMetadata.setParents(Collections.singletonList(folderid));
    
    try {
        FileContent mediaContent = new FileContent(null, file);
      File fileUpload = service.files().create(fileMetadata,mediaContent)
          .setFields("id,name")
          .execute();
      return fileUpload.getId();
    } catch (GoogleJsonResponseException e) {
      // TODO(developer) - handle error appropriately
      JOptionPane.showMessageDialog(null, "Unable to create file: " + e.getDetails());
      throw e;
    }
    
  }
public final void loadFilesType()throws IOException, GeneralSecurityException 
 {
// Build a new authorized API client service.
    final String spreadsheetId = "1CHbGWR1L3KLxpSC6aYcAXTnS0cOeikMPq4jziT67RdY";
    final String range = "Residents_Files_Portofolio!A2:ZZ";
    GoogleCredential credential;
    try (InputStream credentialsStream = LOGIN.class.getResourceAsStream(CREDENTIALS_FILE_PATH_DRIVE)) {
            credential = GoogleCredential.fromStream(credentialsStream);
        }
    credential = credential.createScoped(Collections.singleton(SheetsScopes.SPREADSHEETS));
    Sheets service = new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(), GsonFactory.getDefaultInstance(), credential)
      .setApplicationName(APPLICATION_NAME)
      .build();
    ValueRange response = service.spreadsheets().values()
        .get(spreadsheetId, range)
        .execute();
    List<List<Object>> values = response.getValues();
    if (values == null || values.isEmpty()) {
      JOptionPane.showMessageDialog(null,"No data found.");
    } else {
        filesInfo=values;
        
        for (List row : values)
        {
        // Print columns A and E, which correspond to indices 0 and 4.
        mnFiles.addItem((String) row.get(1));
        }
    } 
 }
public static void loadFiles(String folderid)throws IOException, GeneralSecurityException 
 {
    // List<String> fileNames = new ArrayList<>();
     model=new DefaultTableModel(new String[]{"#","NAME OF FILE","FILE TYPE","DESCRIPTION","REGISTERED ON"}, 0);
    // Load pre-authorized user credentials from the environment.
    // TODO(developer) - See https://developers.google.com/identity for
    // guides on implementing OAuth2 for your application.
    GoogleCredentials credentials;
        try (InputStream credentialsStream = DASHBOARD.class.getResourceAsStream(CREDENTIALS_FILE_PATH_DRIVE)) {
            credentials = GoogleCredentials.fromStream(credentialsStream);
        }
//LOGIN.class.getResourceAsStream(CREDENTIALS_FILE_PATH)
    credentials = credentials.createScoped(Arrays.asList(DriveScopes.DRIVE_FILE));
    HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(
        credentials);
    // Build a new authorized API client service.
    Drive service = new Drive.Builder(new NetHttpTransport(),
        GsonFactory.getDefaultInstance(),
        requestInitializer)
        .setApplicationName(APPLICATION_NAME)
        .build();
    // File's metadata.
    //File fileMetadata = new File();
    //fileMetadata.setName();
    //fileMetadata.setMimeType("application/vnd.google-apps.file");
    //fileMetadata.setParents(Collections.singletonList(folderId));
    try {
        FileList result = service.files().list()
                    //.setPageSize(10)
                    .setQ("'" + folderid + "' in parents")
                    .setFields("files(id,name)")
                    .execute();
            List<File> files = result.getFiles();
            loadedFiles=new ArrayList<>();
            loadedNames=new ArrayList<>();
            if (files != null) {
                int n=1;
                for (File file : files) {
                   if( isFileRecorded(file.getName(),file.getId()))
                   {
                  model.addRow(new Object[] { n,file.getName(),docType,docDesc,regOn });   
                  loadedFiles.add(file.getId());
                   loadedNames.add(file.getName());
                   }
                   else
                   {
                   service.files().delete(file.getId()).execute();
                   }
                   n++;
                    //fileNames.add(file.getName());
                }   
            }
    } catch (GoogleJsonResponseException e) {
      // TODO(developer) - handle error appropriately
      JOptionPane.showMessageDialog(null, "Unable to create file: " + e.getDetails());
      throw e;
    }
 }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel6 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        lbUser = new javax.swing.JLabel();
        lbEmail = new javax.swing.JLabel();
        lbDep = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        lbTitle = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel24 = new javax.swing.JPanel();
        pnEveryone = new javax.swing.JPanel();
        btnMyReport = new javax.swing.JButton();
        btnDashboard = new javax.swing.JButton();
        btnPortofolio1 = new javax.swing.JButton();
        pnHigh = new javax.swing.JPanel();
        btnOtherArchive = new javax.swing.JButton();
        btnOtherArchive1 = new javax.swing.JButton();
        btnCreateFolderArchive = new javax.swing.JButton();
        btnOtherReport = new javax.swing.JButton();
        workPlace = new javax.swing.JTabbedPane();
        pnFileToMyArchive = new javax.swing.JPanel();
        pnAddFileToMyFolder = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        mnFiles = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        btnUploadPortofolioDoc = new javax.swing.JButton();
        lbMsg = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtDescription = new javax.swing.JTextArea();
        jLabel5 = new javax.swing.JLabel();
        btnBrowse = new javax.swing.JButton();
        lbFile = new javax.swing.JLabel();
        nmFiles = new javax.swing.JTextField();
        pnFileFolder = new javax.swing.JPanel();
        pnFileToFolder = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        mnFolders = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        btnUploadPortofolioDoc1 = new javax.swing.JButton();
        lbMsg1 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        fileDescription = new javax.swing.JTextArea();
        jLabel9 = new javax.swing.JLabel();
        btnBrowse1 = new javax.swing.JButton();
        lbFile1 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        fileName = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        mnSubFolders = new javax.swing.JComboBox<>();
        cbFiles = new javax.swing.JComboBox<>();
        jLabel17 = new javax.swing.JLabel();
        txOwner = new javax.swing.JTextField();
        cbDep = new javax.swing.JComboBox<>();
        jLabel29 = new javax.swing.JLabel();
        pnCreateFolderToArchive = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        btnCreateFolder = new javax.swing.JButton();
        lbMsg2 = new javax.swing.JLabel();
        pnAccess = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jlTitles = new javax.swing.JList<>();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jScrollPane7 = new javax.swing.JScrollPane();
        jlGrantedTitles = new javax.swing.JList<>();
        jPanel1 = new javax.swing.JPanel();
        jLabel30 = new javax.swing.JLabel();
        nmFolder = new javax.swing.JTextField();
        cboDepFolder = new javax.swing.JComboBox<>();
        jLabel22 = new javax.swing.JLabel();
        pnMyFiles = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbFiles = new javax.swing.JTable();
        jPanel11 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        lbMsg4 = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        bntViewFile = new javax.swing.JButton();
        btnDownloadFile = new javax.swing.JButton();
        pnFilesAll = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tbFiles1 = new javax.swing.JTable();
        jPanel18 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        cbArchive = new javax.swing.JComboBox<>();
        jLabel20 = new javax.swing.JLabel();
        cbFolder = new javax.swing.JComboBox<>();
        jLabel34 = new javax.swing.JLabel();
        cbDep1 = new javax.swing.JComboBox<>();
        jPanel19 = new javax.swing.JPanel();
        bntViewFile1 = new javax.swing.JButton();
        btnDownloadFile1 = new javax.swing.JButton();
        lbMsg5 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        pnDashboard = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        lblogo = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        lbimage = new javax.swing.JLabel();
        pnAddNewUser = new javax.swing.JPanel();
        jPanel23 = new javax.swing.JPanel();
        jLabel31 = new javax.swing.JLabel();
        btnuser = new javax.swing.JButton();
        lbMsg6 = new javax.swing.JLabel();
        btnesign = new javax.swing.JButton();
        txlastname = new javax.swing.JTextField();
        txname = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        txphone = new javax.swing.JTextField();
        jLabel38 = new javax.swing.JLabel();
        txemail = new javax.swing.JTextField();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        cbsex = new javax.swing.JComboBox<>();
        txdate = new com.toedter.calendar.JDateChooser();
        jLabel45 = new javax.swing.JLabel();
        btnphoto = new javax.swing.JButton();
        cbdep = new javax.swing.JComboBox<>();
        cbfunct = new javax.swing.JComboBox<>();
        txtitle = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setLocation(new java.awt.Point(150, 0));
        setResizable(false);

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Main Window", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Calisto MT", 1, 24))); // NOI18N

        jPanel2.setBackground(new java.awt.Color(0, 153, 153));
        jPanel2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jPanel5.setBackground(new java.awt.Color(204, 204, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lbUser.setFont(new java.awt.Font("Calisto MT", 1, 12)); // NOI18N
        lbUser.setText("USER:");

        lbEmail.setFont(new java.awt.Font("Calisto MT", 1, 12)); // NOI18N
        lbEmail.setText("EMAIL:");

        lbDep.setFont(new java.awt.Font("Calisto MT", 1, 12)); // NOI18N
        lbDep.setText("DEPARTMENT:");

        jLabel13.setBackground(new java.awt.Color(255, 255, 255));
        jLabel13.setFont(new java.awt.Font("Calisto MT", 1, 12)); // NOI18N
        jLabel13.setText("RIIO APP");

        jLabel14.setBackground(new java.awt.Color(255, 255, 255));
        jLabel14.setFont(new java.awt.Font("Calisto MT", 1, 12)); // NOI18N
        jLabel14.setText("V 1.0.0");

        lbTitle.setFont(new java.awt.Font("Calisto MT", 1, 12)); // NOI18N
        lbTitle.setText("TITLE:");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lbDep, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbUser, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbEmail, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbTitle, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbUser, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lbEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lbDep, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lbTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(0, 153, 153));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jPanel24.setBackground(new java.awt.Color(0, 153, 153));

        pnEveryone.setBackground(new java.awt.Color(0, 153, 153));

        btnMyReport.setBackground(new java.awt.Color(204, 204, 255));
        btnMyReport.setFont(new java.awt.Font("Calisto MT", 1, 12)); // NOI18N
        btnMyReport.setText("MY ARCHIVE / PORTOFOLIO FILES LIST");
        btnMyReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMyReportActionPerformed(evt);
            }
        });

        btnDashboard.setBackground(new java.awt.Color(204, 204, 255));
        btnDashboard.setFont(new java.awt.Font("Calisto MT", 1, 12)); // NOI18N
        btnDashboard.setText("HOME");
        btnDashboard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDashboardActionPerformed(evt);
            }
        });

        btnPortofolio1.setBackground(new java.awt.Color(204, 204, 255));
        btnPortofolio1.setFont(new java.awt.Font("Calisto MT", 1, 12)); // NOI18N
        btnPortofolio1.setText("ADD FILE TO MY ARCHIVE / PORTOFOLIO");
        btnPortofolio1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPortofolio1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnEveryoneLayout = new javax.swing.GroupLayout(pnEveryone);
        pnEveryone.setLayout(pnEveryoneLayout);
        pnEveryoneLayout.setHorizontalGroup(
            pnEveryoneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnEveryoneLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnEveryoneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnMyReport, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnDashboard, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnPortofolio1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnEveryoneLayout.setVerticalGroup(
            pnEveryoneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnEveryoneLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnDashboard, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnPortofolio1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnMyReport, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnHigh.setBackground(new java.awt.Color(0, 153, 153));

        btnOtherArchive.setBackground(new java.awt.Color(204, 204, 255));
        btnOtherArchive.setFont(new java.awt.Font("Calisto MT", 1, 12)); // NOI18N
        btnOtherArchive.setText("ADD FILE TO ARCHIVE ");
        btnOtherArchive.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOtherArchiveActionPerformed(evt);
            }
        });

        btnOtherArchive1.setBackground(new java.awt.Color(204, 204, 255));
        btnOtherArchive1.setFont(new java.awt.Font("Calisto MT", 1, 12)); // NOI18N
        btnOtherArchive1.setText("ADD NEW USER");
        btnOtherArchive1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOtherArchive1ActionPerformed(evt);
            }
        });

        btnCreateFolderArchive.setBackground(new java.awt.Color(204, 204, 255));
        btnCreateFolderArchive.setFont(new java.awt.Font("Calisto MT", 1, 12)); // NOI18N
        btnCreateFolderArchive.setText("CREATE FOLDER INTO ADMINISTRATION ARCHIVE");
        btnCreateFolderArchive.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreateFolderArchiveActionPerformed(evt);
            }
        });

        btnOtherReport.setBackground(new java.awt.Color(204, 204, 255));
        btnOtherReport.setFont(new java.awt.Font("Calisto MT", 1, 12)); // NOI18N
        btnOtherReport.setText("ADMINISTRATION ARCHIVE FILES LIST");
        btnOtherReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOtherReportActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnHighLayout = new javax.swing.GroupLayout(pnHigh);
        pnHigh.setLayout(pnHighLayout);
        pnHighLayout.setHorizontalGroup(
            pnHighLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnHighLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnHighLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnCreateFolderArchive, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnOtherReport, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnOtherArchive, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnOtherArchive1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnHighLayout.setVerticalGroup(
            pnHighLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnHighLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnOtherArchive1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnOtherArchive, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCreateFolderArchive, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnOtherReport, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnHigh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnEveryone, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addComponent(pnEveryone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnHigh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        workPlace.setBackground(new java.awt.Color(0, 153, 153));
        workPlace.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        pnFileToMyArchive.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        mnFiles.setFont(new java.awt.Font("Calisto MT", 0, 12)); // NOI18N
        mnFiles.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Type Of Document" }));
        mnFiles.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                mnFilesItemStateChanged(evt);
            }
        });
        mnFiles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnFilesActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Calisto MT", 1, 12)); // NOI18N
        jLabel2.setText("NAME OF FILE");

        btnUploadPortofolioDoc.setBackground(new java.awt.Color(167, 108, 108));
        btnUploadPortofolioDoc.setFont(new java.awt.Font("Calisto MT", 1, 14)); // NOI18N
        btnUploadPortofolioDoc.setText("Upload Document");
        btnUploadPortofolioDoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUploadPortofolioDocActionPerformed(evt);
            }
        });

        lbMsg.setFont(new java.awt.Font("Calisto MT", 0, 12)); // NOI18N
        lbMsg.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jLabel12.setFont(new java.awt.Font("Calisto MT", 1, 12)); // NOI18N
        jLabel12.setText("FILE DESCRIPTION");

        txtDescription.setEditable(false);
        txtDescription.setColumns(20);
        txtDescription.setFont(new java.awt.Font("Calisto MT", 0, 12)); // NOI18N
        txtDescription.setRows(100);
        jScrollPane1.setViewportView(txtDescription);

        jLabel5.setFont(new java.awt.Font("Calisto MT", 1, 12)); // NOI18N
        jLabel5.setText("FILE");

        btnBrowse.setBackground(new java.awt.Color(102, 153, 255));
        btnBrowse.setFont(new java.awt.Font("Calisto MT", 1, 14)); // NOI18N
        btnBrowse.setText("Browse File");
        btnBrowse.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnBrowse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBrowseActionPerformed(evt);
            }
        });

        lbFile.setBackground(new java.awt.Color(204, 204, 255));
        lbFile.setFont(new java.awt.Font("Calisto MT", 0, 12)); // NOI18N
        lbFile.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        nmFiles.setFont(new java.awt.Font("Calisto MT", 0, 12)); // NOI18N

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbMsg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(nmFiles)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 375, Short.MAX_VALUE)
                            .addComponent(mnFiles, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnUploadPortofolioDoc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnBrowse, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lbFile, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(10, 10, 10))))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(mnFiles, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nmFiles, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBrowse))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbFile, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnUploadPortofolioDoc, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbMsg, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnAddFileToMyFolderLayout = new javax.swing.GroupLayout(pnAddFileToMyFolder);
        pnAddFileToMyFolder.setLayout(pnAddFileToMyFolderLayout);
        pnAddFileToMyFolderLayout.setHorizontalGroup(
            pnAddFileToMyFolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnAddFileToMyFolderLayout.createSequentialGroup()
                .addGap(64, 64, 64)
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(70, 70, 70))
        );
        pnAddFileToMyFolderLayout.setVerticalGroup(
            pnAddFileToMyFolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnAddFileToMyFolderLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout pnFileToMyArchiveLayout = new javax.swing.GroupLayout(pnFileToMyArchive);
        pnFileToMyArchive.setLayout(pnFileToMyArchiveLayout);
        pnFileToMyArchiveLayout.setHorizontalGroup(
            pnFileToMyArchiveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnAddFileToMyFolder, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnFileToMyArchiveLayout.setVerticalGroup(
            pnFileToMyArchiveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnFileToMyArchiveLayout.createSequentialGroup()
                .addComponent(pnAddFileToMyFolder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        workPlace.addTab("", pnFileToMyArchive);

        pnFileFolder.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        mnFolders.setFont(new java.awt.Font("Calisto MT", 0, 12)); // NOI18N
        mnFolders.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Archive", "Human Resource Archive", "Administration Archive" }));
        mnFolders.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                mnFoldersItemStateChanged(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Calisto MT", 1, 12)); // NOI18N
        jLabel3.setText("ARCHIVE");

        btnUploadPortofolioDoc1.setBackground(new java.awt.Color(167, 108, 108));
        btnUploadPortofolioDoc1.setFont(new java.awt.Font("Calisto MT", 1, 14)); // NOI18N
        btnUploadPortofolioDoc1.setText("Upload Document");
        btnUploadPortofolioDoc1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUploadPortofolioDoc1ActionPerformed(evt);
            }
        });

        lbMsg1.setFont(new java.awt.Font("Calisto MT", 0, 12)); // NOI18N
        lbMsg1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jLabel15.setFont(new java.awt.Font("Calisto MT", 1, 12)); // NOI18N
        jLabel15.setText("FILE DESCRIPTION");

        fileDescription.setColumns(20);
        fileDescription.setFont(new java.awt.Font("Calisto MT", 0, 12)); // NOI18N
        fileDescription.setRows(100);
        jScrollPane3.setViewportView(fileDescription);

        jLabel9.setFont(new java.awt.Font("Calisto MT", 1, 12)); // NOI18N
        jLabel9.setText("FILE");

        btnBrowse1.setBackground(new java.awt.Color(102, 153, 255));
        btnBrowse1.setFont(new java.awt.Font("Calisto MT", 1, 14)); // NOI18N
        btnBrowse1.setText("Browse File");
        btnBrowse1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnBrowse1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBrowse1ActionPerformed(evt);
            }
        });

        lbFile1.setBackground(new java.awt.Color(204, 204, 255));
        lbFile1.setFont(new java.awt.Font("Calisto MT", 0, 12)); // NOI18N
        lbFile1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        jLabel11.setFont(new java.awt.Font("Calisto MT", 1, 12)); // NOI18N
        jLabel11.setText("NAME OF FILE");

        fileName.setEditable(false);
        fileName.setFont(new java.awt.Font("Calisto MT", 0, 12)); // NOI18N
        fileName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileNameActionPerformed(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Calisto MT", 1, 12)); // NOI18N
        jLabel16.setText("FOLDER");

        mnSubFolders.setFont(new java.awt.Font("Calisto MT", 0, 12)); // NOI18N
        mnSubFolders.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                mnSubFoldersItemStateChanged(evt);
            }
        });

        cbFiles.setFont(new java.awt.Font("Calisto MT", 0, 12)); // NOI18N
        cbFiles.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbFilesItemStateChanged(evt);
            }
        });

        jLabel17.setFont(new java.awt.Font("Calisto MT", 1, 12)); // NOI18N
        jLabel17.setText("NAME OF OWNER");

        txOwner.setEditable(false);
        txOwner.setFont(new java.awt.Font("Calisto MT", 0, 12)); // NOI18N
        txOwner.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txOwnerActionPerformed(evt);
            }
        });

        cbDep.setFont(new java.awt.Font("Calisto MT", 0, 12)); // NOI18N
        cbDep.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Department", "RIIO NGO", "RIIO SCHOOL", "RIIO iHOSPITAL", "RIIO iCHECKS" }));

        jLabel29.setFont(new java.awt.Font("Calisto MT", 1, 12)); // NOI18N
        jLabel29.setText("DEPARTMENT");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbMsg1, javax.swing.GroupLayout.PREFERRED_SIZE, 569, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnUploadPortofolioDoc1, javax.swing.GroupLayout.PREFERRED_SIZE, 389, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbFile1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cbFiles, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(mnFolders, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(mnSubFolders, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txOwner)
                            .addComponent(fileName)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 390, Short.MAX_VALUE)
                            .addComponent(btnBrowse1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cbDep, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(9, Short.MAX_VALUE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbDep, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(mnFolders)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(mnSubFolders, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txOwner, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fileName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbFiles, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBrowse1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbFile1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnUploadPortofolioDoc1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbMsg1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(34, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnFileToFolderLayout = new javax.swing.GroupLayout(pnFileToFolder);
        pnFileToFolder.setLayout(pnFileToFolderLayout);
        pnFileToFolderLayout.setHorizontalGroup(
            pnFileToFolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnFileToFolderLayout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(81, Short.MAX_VALUE))
        );
        pnFileToFolderLayout.setVerticalGroup(
            pnFileToFolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnFileToFolderLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnFileFolderLayout = new javax.swing.GroupLayout(pnFileFolder);
        pnFileFolder.setLayout(pnFileFolderLayout);
        pnFileFolderLayout.setHorizontalGroup(
            pnFileFolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnFileToFolder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnFileFolderLayout.setVerticalGroup(
            pnFileFolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnFileFolderLayout.createSequentialGroup()
                .addComponent(pnFileToFolder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        workPlace.addTab("", pnFileFolder);

        pnCreateFolderToArchive.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        pnCreateFolderToArchive.setFont(new java.awt.Font("Calisto MT", 0, 12)); // NOI18N

        btnCreateFolder.setBackground(new java.awt.Color(167, 108, 108));
        btnCreateFolder.setFont(new java.awt.Font("Calisto MT", 1, 14)); // NOI18N
        btnCreateFolder.setText("Create Folder");
        btnCreateFolder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreateFolderActionPerformed(evt);
            }
        });

        lbMsg2.setFont(new java.awt.Font("Calisto MT", 0, 12)); // NOI18N
        lbMsg2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        pnAccess.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Calisto MT", 1, 12))); // NOI18N

        jlTitles.setFont(new java.awt.Font("Calisto MT", 0, 12)); // NOI18N
        jlTitles.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jlTitlesMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(jlTitles);

        jLabel32.setFont(new java.awt.Font("Calisto MT", 1, 14)); // NOI18N
        jLabel32.setText("Grant Access To");

        jLabel33.setFont(new java.awt.Font("Calisto MT", 1, 14)); // NOI18N
        jLabel33.setText("Granted Access");

        jButton1.setBackground(new java.awt.Color(255, 153, 153));
        jButton1.setFont(new java.awt.Font("Calisto MT", 1, 12)); // NOI18N
        jButton1.setText("REMOVE");

        jButton2.setBackground(new java.awt.Color(153, 255, 153));
        jButton2.setFont(new java.awt.Font("Calisto MT", 1, 12)); // NOI18N
        jButton2.setText("GRANT");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jlGrantedTitles.setFont(new java.awt.Font("Calisto MT", 0, 12)); // NOI18N
        jScrollPane7.setViewportView(jlGrantedTitles);

        javax.swing.GroupLayout pnAccessLayout = new javax.swing.GroupLayout(pnAccess);
        pnAccess.setLayout(pnAccessLayout);
        pnAccessLayout.setHorizontalGroup(
            pnAccessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnAccessLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(pnAccessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel32, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
                    .addComponent(jLabel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnAccessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(pnAccessLayout.createSequentialGroup()
                        .addComponent(jScrollPane7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnAccessLayout.createSequentialGroup()
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        pnAccessLayout.setVerticalGroup(
            pnAccessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnAccessLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnAccessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnAccessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnAccessLayout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jLabel30.setFont(new java.awt.Font("Calisto MT", 1, 12)); // NOI18N
        jLabel30.setText("DEPARTMENT");

        nmFolder.setFont(new java.awt.Font("Calisto MT", 0, 12)); // NOI18N
        nmFolder.setText("Enter Folder Name");
        nmFolder.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                nmFolderMouseClicked(evt);
            }
        });

        cboDepFolder.setFont(new java.awt.Font("Calisto MT", 0, 12)); // NOI18N
        cboDepFolder.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Department", "RIIO NGO", "RIIO SCHOOL", "RIIO iHOSPITAL", "RIIO iCHECKS" }));
        cboDepFolder.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboDepFolderItemStateChanged(evt);
            }
        });

        jLabel22.setFont(new java.awt.Font("Calisto MT", 1, 12)); // NOI18N
        jLabel22.setText("FOLDER NAME");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cboDepFolder, 0, 448, Short.MAX_VALUE)
                    .addComponent(nmFolder))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 12, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboDepFolder, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nmFolder, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCreateFolder, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(144, 144, 144))
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(lbMsg2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel15Layout.createSequentialGroup()
                                .addGap(0, 75, Short.MAX_VALUE)
                                .addComponent(pnAccess, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(35, 35, 35))))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnAccess, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCreateFolder, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
                .addComponent(lbMsg2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout pnCreateFolderToArchiveLayout = new javax.swing.GroupLayout(pnCreateFolderToArchive);
        pnCreateFolderToArchive.setLayout(pnCreateFolderToArchiveLayout);
        pnCreateFolderToArchiveLayout.setHorizontalGroup(
            pnCreateFolderToArchiveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnCreateFolderToArchiveLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnCreateFolderToArchiveLayout.setVerticalGroup(
            pnCreateFolderToArchiveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnCreateFolderToArchiveLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        workPlace.addTab("", pnCreateFolderToArchive);

        pnMyFiles.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        tbFiles.setBackground(new java.awt.Color(242, 242, 242));
        tbFiles.setFont(new java.awt.Font("Calisto MT", 0, 12)); // NOI18N
        tbFiles.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(tbFiles);

        jLabel6.setFont(new java.awt.Font("Calisto MT", 0, 12)); // NOI18N
        jLabel6.setText("Search Filename:");

        jTextField1.setFont(new java.awt.Font("Calisto MT", 0, 12)); // NOI18N
        jTextField1.setText("Enter searck key");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        lbMsg4.setFont(new java.awt.Font("Calisto MT", 0, 12)); // NOI18N
        lbMsg4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        bntViewFile.setBackground(new java.awt.Color(0, 204, 0));
        bntViewFile.setFont(new java.awt.Font("Calisto MT", 1, 12)); // NOI18N
        bntViewFile.setText("View");

        btnDownloadFile.setBackground(new java.awt.Color(153, 255, 153));
        btnDownloadFile.setFont(new java.awt.Font("Calisto MT", 1, 12)); // NOI18N
        btnDownloadFile.setText("Download");
        btnDownloadFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDownloadFileActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(bntViewFile, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnDownloadFile, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDownloadFile, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bntViewFile, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout pnMyFilesLayout = new javax.swing.GroupLayout(pnMyFiles);
        pnMyFiles.setLayout(pnMyFilesLayout);
        pnMyFilesLayout.setHorizontalGroup(
            pnMyFilesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnMyFilesLayout.createSequentialGroup()
                .addGap(91, 91, 91)
                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(91, 91, 91))
            .addGroup(pnMyFilesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnMyFilesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbMsg4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 691, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnMyFilesLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        pnMyFilesLayout.setVerticalGroup(
            pnMyFilesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnMyFilesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lbMsg4, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        workPlace.addTab("", pnMyFiles);

        pnFilesAll.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        tbFiles1.setBackground(new java.awt.Color(242, 242, 242));
        tbFiles1.setFont(new java.awt.Font("Calisto MT", 0, 12)); // NOI18N
        tbFiles1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane4.setViewportView(tbFiles1);

        jPanel18.setBackground(new java.awt.Color(204, 204, 204));
        jPanel18.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "SELECT", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Calisto MT", 1, 18))); // NOI18N

        jLabel8.setFont(new java.awt.Font("Calisto MT", 0, 12)); // NOI18N
        jLabel8.setText("Search Filename:");

        jTextField2.setFont(new java.awt.Font("Calisto MT", 0, 12)); // NOI18N
        jTextField2.setText("Enter searck key");

        jLabel19.setFont(new java.awt.Font("Calisto MT", 0, 12)); // NOI18N
        jLabel19.setText("Select Archive");

        cbArchive.setFont(new java.awt.Font("Calisto MT", 0, 12)); // NOI18N
        cbArchive.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Archive", "Human Resource Archive", "Administration Archive" }));
        cbArchive.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbArchiveItemStateChanged(evt);
            }
        });
        cbArchive.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbArchiveActionPerformed(evt);
            }
        });

        jLabel20.setFont(new java.awt.Font("Calisto MT", 0, 12)); // NOI18N
        jLabel20.setText("Select Folder:");

        cbFolder.setFont(new java.awt.Font("Calisto MT", 0, 12)); // NOI18N
        cbFolder.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Folder" }));
        cbFolder.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbFolderItemStateChanged(evt);
            }
        });

        jLabel34.setFont(new java.awt.Font("Calisto MT", 0, 12)); // NOI18N
        jLabel34.setText("Select Department");

        cbDep1.setFont(new java.awt.Font("Calisto MT", 0, 12)); // NOI18N
        cbDep1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Department", "RIIO NGO", "RIIO SCHOOL", "RIIO iHOSPITAL", "RIIO iCHECKS" }));

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(cbFolder, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cbArchive, 0, 241, Short.MAX_VALUE)))
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbDep1, 0, 241, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextField2, javax.swing.GroupLayout.DEFAULT_SIZE, 258, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(128, 128, 128))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbDep1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(9, 9, 9)
                        .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbArchive, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel18Layout.createSequentialGroup()
                            .addGap(36, 36, 36)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbFolder, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19))
        );

        bntViewFile1.setBackground(new java.awt.Color(0, 204, 0));
        bntViewFile1.setFont(new java.awt.Font("Calisto MT", 1, 12)); // NOI18N
        bntViewFile1.setText("View");

        btnDownloadFile1.setBackground(new java.awt.Color(153, 255, 153));
        btnDownloadFile1.setFont(new java.awt.Font("Calisto MT", 1, 12)); // NOI18N
        btnDownloadFile1.setText("Download");
        btnDownloadFile1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDownloadFile1ActionPerformed(evt);
            }
        });

        lbMsg5.setFont(new java.awt.Font("Calisto MT", 0, 12)); // NOI18N
        lbMsg5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbMsg5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel19Layout.createSequentialGroup()
                        .addGap(107, 107, 107)
                        .addComponent(bntViewFile1, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDownloadFile1, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDownloadFile1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bntViewFile1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbMsg5, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jLabel18.setFont(new java.awt.Font("Calisto MT", 1, 14)); // NOI18N
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setText("LIST OF FILES");

        javax.swing.GroupLayout pnFilesAllLayout = new javax.swing.GroupLayout(pnFilesAll);
        pnFilesAll.setLayout(pnFilesAllLayout);
        pnFilesAllLayout.setHorizontalGroup(
            pnFilesAllLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnFilesAllLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(pnFilesAllLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, 679, Short.MAX_VALUE)
                    .addComponent(jPanel19, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 679, Short.MAX_VALUE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(12, 12, 12))
        );
        pnFilesAllLayout.setVerticalGroup(
            pnFilesAllLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnFilesAllLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        workPlace.addTab("", pnFilesAll);

        pnDashboard.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel21.setFont(new java.awt.Font("Calisto MT", 2, 100)); // NOI18N
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("HOME");

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblogo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, 679, Short.MAX_VALUE)
                    .addComponent(lbimage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel21)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbimage, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblogo, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12))
        );

        javax.swing.GroupLayout pnDashboardLayout = new javax.swing.GroupLayout(pnDashboard);
        pnDashboard.setLayout(pnDashboardLayout);
        pnDashboardLayout.setHorizontalGroup(
            pnDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnDashboardLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnDashboardLayout.setVerticalGroup(
            pnDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnDashboardLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        workPlace.addTab("", pnDashboard);

        pnAddNewUser.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel31.setFont(new java.awt.Font("Calisto MT", 1, 12)); // NOI18N
        jLabel31.setText("FIRST NAME");

        btnuser.setBackground(new java.awt.Color(167, 108, 108));
        btnuser.setFont(new java.awt.Font("Calisto MT", 1, 14)); // NOI18N
        btnuser.setText("Save User");
        btnuser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnuserActionPerformed(evt);
            }
        });

        lbMsg6.setFont(new java.awt.Font("Calisto MT", 0, 12)); // NOI18N
        lbMsg6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        btnesign.setBackground(new java.awt.Color(102, 153, 255));
        btnesign.setFont(new java.awt.Font("Calisto MT", 1, 14)); // NOI18N
        btnesign.setText("Browse File");
        btnesign.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnesign.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnesignActionPerformed(evt);
            }
        });

        txlastname.setFont(new java.awt.Font("Calisto MT", 0, 12)); // NOI18N

        txname.setFont(new java.awt.Font("Calisto MT", 0, 12)); // NOI18N

        jLabel35.setFont(new java.awt.Font("Calisto MT", 1, 12)); // NOI18N
        jLabel35.setText("GENDER");

        jLabel36.setFont(new java.awt.Font("Calisto MT", 1, 12)); // NOI18N
        jLabel36.setText("LAST NAME");

        jLabel37.setFont(new java.awt.Font("Calisto MT", 1, 12)); // NOI18N
        jLabel37.setText("TELEPHONE");

        txphone.setFont(new java.awt.Font("Calisto MT", 0, 12)); // NOI18N

        jLabel38.setFont(new java.awt.Font("Calisto MT", 1, 12)); // NOI18N
        jLabel38.setText("DEPARTMENT");

        txemail.setFont(new java.awt.Font("Calisto MT", 0, 12)); // NOI18N

        jLabel40.setFont(new java.awt.Font("Calisto MT", 1, 12)); // NOI18N
        jLabel40.setText("JOIN DATE");

        jLabel41.setFont(new java.awt.Font("Calisto MT", 1, 12)); // NOI18N
        jLabel41.setText("FUNCTION");

        jLabel42.setFont(new java.awt.Font("Calisto MT", 1, 12)); // NOI18N
        jLabel42.setText("E-SIGNATURE");

        jLabel43.setFont(new java.awt.Font("Calisto MT", 1, 12)); // NOI18N
        jLabel43.setText("TITLE");

        jLabel44.setFont(new java.awt.Font("Calisto MT", 1, 12)); // NOI18N
        jLabel44.setText("PHOTO PASSPORT");

        cbsex.setFont(new java.awt.Font("Calisto MT", 0, 12)); // NOI18N
        cbsex.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Gender", "Male", "Female" }));

        jLabel45.setFont(new java.awt.Font("Calisto MT", 1, 12)); // NOI18N
        jLabel45.setText("EMAIL");

        btnphoto.setBackground(new java.awt.Color(102, 153, 255));
        btnphoto.setFont(new java.awt.Font("Calisto MT", 1, 14)); // NOI18N
        btnphoto.setText("Browse File");
        btnphoto.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnphoto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnphotoActionPerformed(evt);
            }
        });

        cbdep.setFont(new java.awt.Font("Calisto MT", 0, 12)); // NOI18N
        cbdep.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Department", "RIIO NGO", "RIIO SCHOOL", "RIIO iHOSPITAL", "RIIO iCHECKS" }));

        cbfunct.setFont(new java.awt.Font("Calisto MT", 0, 12)); // NOI18N
        cbfunct.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Function", "Director", "Manager", "Officer", "Assistant Officer", "Resident" }));

        txtitle.setFont(new java.awt.Font("Calisto MT", 0, 12)); // NOI18N
        txtitle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtitleActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel23Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lbMsg6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel42, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel44, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnphoto, javax.swing.GroupLayout.DEFAULT_SIZE, 499, Short.MAX_VALUE)
                            .addComponent(btnesign, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnuser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel23Layout.createSequentialGroup()
                        .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel37, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel31, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
                                .addComponent(jLabel35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel36, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel45, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel40, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel43, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel38, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
                                .addComponent(jLabel41, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txname, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txdate, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txemail)
                            .addComponent(txphone)
                            .addComponent(cbsex, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txlastname)
                            .addComponent(cbdep, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cbfunct, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtitle))))
                .addGap(12, 12, 12))
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(txname, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE))
                    .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txlastname, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbsex, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txphone, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txemail, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel45, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txdate, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbdep, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbfunct, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtitle, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnesign, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnphoto, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnuser, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbMsg6, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnAddNewUserLayout = new javax.swing.GroupLayout(pnAddNewUser);
        pnAddNewUser.setLayout(pnAddNewUserLayout);
        pnAddNewUserLayout.setHorizontalGroup(
            pnAddNewUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnAddNewUserLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnAddNewUserLayout.setVerticalGroup(
            pnAddNewUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnAddNewUserLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        workPlace.addTab("", pnAddNewUser);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(workPlace, javax.swing.GroupLayout.PREFERRED_SIZE, 705, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(workPlace, javax.swing.GroupLayout.PREFERRED_SIZE, 600, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnDashboardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDashboardActionPerformed
        // TODO add your handling code here:
      workPlace.removeAll();
     workPlace.add(pnDashboard);
     workPlace.setTitleAt(0, "HOME");
        
    }//GEN-LAST:event_btnDashboardActionPerformed
    private void btnMyReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMyReportActionPerformed
        // TODO add your handling code here:
        tbFiles.removeAll();
        workPlace.removeAll();
       try
        {  
      loadFiles(folderId);
        }
      catch(IOException | GeneralSecurityException e)
        {
      JOptionPane.showMessageDialog(null, e);
        }
       tbFiles.setModel(model);
        
     workPlace.add(pnMyFiles);
     workPlace.setTitleAt(0, "LIST OF MY FILES"); 
    }//GEN-LAST:event_btnMyReportActionPerformed

    private void btnUploadPortofolioDocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUploadPortofolioDocActionPerformed
        // TODO add your handling code here:
            try {
                Date d=new Date();
                SimpleDateFormat dte = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");
                String filePath=null;
                if(mnFiles.getSelectedIndex()>0)
                {
                filePath=(String) mnFiles.getSelectedItem();
                }
                else
                {
                filePath=(String) nmFiles.getText();
                }
                String ext="";
        String namefile=selectedFile.getName();
     if (namefile.lastIndexOf(".") != -1 && namefile.lastIndexOf(".") != 0) {
           ext=namefile.substring(namefile.lastIndexOf(".") + 1);
           
        }
                filePath=filePath.replace(" ", "_");
                filePath=filePath+"_"+names+"_"+dte.format(d)+"."+ext;
                
                String id=uploadFile(filePath,selectedFile,folderId);
                final String spreadsheetId = "1CHbGWR1L3KLxpSC6aYcAXTnS0cOeikMPq4jziT67RdY";
    final String range = "Files_Archive_Info";
    GoogleCredential credential;
        try (InputStream credentialsStream = DASHBOARD.class.getResourceAsStream(CREDENTIALS_FILE_PATH_DRIVE)) {
            credential = GoogleCredential.fromStream(credentialsStream);
        }
    credential = credential.createScoped(Collections.singleton(SheetsScopes.SPREADSHEETS));
    Sheets service = new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, credential)
      .setApplicationName(APPLICATION_NAME)
      .build();
    ValueRange response = service.spreadsheets().values()
        .get(spreadsheetId, range)
        .execute();
    List<List<Object>> values = response.getValues();
    if (values == null || values.isEmpty()) {
      JOptionPane.showMessageDialog(null,"No data found.");
    }
    else
    {
        dte = new SimpleDateFormat("dd-MM-yyyy");
        int ro=values.size();
        if(title.equals("Resident"))
                {
            ValueRange body = new ValueRange()
            .setValues(Arrays.asList(Arrays.asList(usercode,names,"User",filePath,(String) mnFiles.getSelectedItem(),id,dte.format(d),txtDescription.getText())));
            // Write the data to the specified range in the spreadsheet
            service.spreadsheets().values()
            .update(spreadsheetId,"Files_Archive_Info!A"+(ro+1)+":H"+(ro+1), body)
            .setValueInputOption("RAW")
            .execute();
            }
        else
        {
        ValueRange body = new ValueRange()
            .setValues(Arrays.asList(Arrays.asList(usercode,names,"User",filePath,(String) nmFiles.getText(),id,dte.format(d),txtDescription.getText())));
            // Write the data to the specified range in the spreadsheet
            service.spreadsheets().values()
            .update(spreadsheetId,"Files_Archive_Info!A"+(ro+1)+":H"+(ro+1), body)
            .setValueInputOption("RAW")
            .execute();    
        }
         lbMsg.setText("File successfully uploaded to your archive / portofolio");
            
    }
                lbMsg.setText("File uploaded successfully");
                lbMsg.setForeground(Color.blue);
            } catch (IOException | GeneralSecurityException ex) {
                JOptionPane.showMessageDialog(null, ex);
            }
        
    }//GEN-LAST:event_btnUploadPortofolioDocActionPerformed

    private void btnBrowseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBrowseActionPerformed
        // TODO add your handling code here:
        JFileChooser fileChooser = new JFileChooser();
        // Set a file filter to only show certain file types
        // FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
        // fileChooser.setFileFilter(filter);
        int returnValue=fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION)
        {
            selectedFile = fileChooser.getSelectedFile();
            lbFile.setText("Selected file: " + selectedFile.getAbsolutePath());
            lbFile.setForeground(Color.blue);
            selectedFile.getAbsolutePath();
        }
    }//GEN-LAST:event_btnBrowseActionPerformed

    private void btnCreateFolderArchiveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateFolderArchiveActionPerformed
        // TODO add your handling code here:
        workPlace.removeAll();
     workPlace.add(pnCreateFolderToArchive);
     workPlace.setTitleAt(0, "CREATE A FOLDER INTO ADMINISTRATION ARCHIVE");
     
     if(!title.equals("Administrator") && !title.equals("Director"))
     {
         cboDepFolder.removeAllItems();
     cboDepFolder.addItem("Select Department");
         cboDepFolder.addItem(department);
     }
    }//GEN-LAST:event_btnCreateFolderArchiveActionPerformed

    private void btnUploadPortofolioDoc1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUploadPortofolioDoc1ActionPerformed
        // TODO add your handling code here:
        String ownerId,filename,filedesc;
        if(mnFolders.getItemCount()>0 && !mnFolders.getSelectedItem().equals("Select Archive"))
        {
            if(mnSubFolders.getItemCount()>0 && !mnSubFolders.getSelectedItem().equals("Select Folder"))
            {
                if(mnFolders.getSelectedItem().equals("Human Resource Archive"))
                {   
            if(typeUser.get(mnSubFolders.getSelectedIndex()-1).equals("Resident"))
                {
                    if(cbFiles.getItemCount()>0 && cbFiles.getSelectedItem().equals("Select Type Of File"))
                    {
                    filedesc=fileDescription.getText();
                    ownerId=SubFoldersId.get(mnSubFolders.getSelectedIndex()-1);
                    filename=(String) cbFiles.getSelectedItem();
                    uploadFiletoFolder(ownerId,(String) cbFiles.getSelectedItem(),filename,filedesc,"User");
                    }
                }
                else
                {
                 if(fileName.getText()!=null)
                    {
                    ownerId=SubFoldersId.get(mnSubFolders.getSelectedIndex()-1);
                    filename=fileName.getText();
                    filedesc=fileDescription.getText();
                    uploadFiletoFolder(ownerId,filename,filename,filedesc,"User");
                    } 
                }   
               }
                else
                {
                 if(fileName.getText()!=null)
                    {
                    ownerId=SubFoldersId.get(mnSubFolders.getSelectedIndex()-1);
                    filename=fileName.getText();
                    filedesc=fileDescription.getText();
                    uploadFiletoFolder(ownerId,filename,filename,filedesc,"Administration");
                    }    
                }
                
          ////////////////////////////////////////////////////////////////////////////////
          
          ////////////////////////////////////////////////////////////////////////////////
                
                
                
            }
        }
        
    }//GEN-LAST:event_btnUploadPortofolioDoc1ActionPerformed

    private void btnBrowse1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBrowse1ActionPerformed
        // TODO add your handling code here:
         JFileChooser fileChooserAll = new JFileChooser();
        // Set a file filter to only show certain file types
        // FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
        // fileChooser.setFileFilter(filter);
        int returnValue=fileChooserAll.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION)
        {
            selectedFileAll = fileChooserAll.getSelectedFile();
            lbFile1.setText("Selected file: " + selectedFileAll.getAbsolutePath());
            lbFile1.setForeground(Color.blue);
            selectedFileAll.getAbsolutePath();
        }
    }//GEN-LAST:event_btnBrowse1ActionPerformed

    private void fileNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fileNameActionPerformed

    private void mnFoldersItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_mnFoldersItemStateChanged
        // TODO add your handling code here:
        if(cbDep.getSelectedIndex()>0)
        {
        String dep=String.valueOf(cbDep.getSelectedItem());
        mnSubFolders.removeAllItems();
        if(mnFolders.getSelectedItem().equals("Human Resource Archive"))
        {
            
            try {  
                if(loadFolders("1vBhrfeof4YDKCrKB5DuITujQCfuRv3jw","User",dep))
                {
                mnSubFolders.setModel(modelSubFolders);
                }
            } catch (IOException | GeneralSecurityException ex) {
                JOptionPane.showMessageDialog(null, ex);
            } 
        }
        else if(mnFolders.getSelectedItem().equals("Administration Archive"))
        {
            try {  
                if(loadFolders("1F4i3S5iSRp55xiRwilMux_07-qonOh4T","Administration",dep));
                {
                mnSubFolders.setModel(modelSubFolders);
                }              
            } catch (IOException | GeneralSecurityException ex) {
                JOptionPane.showMessageDialog(null, ex);
            }
        }
        else
        {
            mnSubFolders.removeAllItems();
        }
        }
        else
        {
           lbMsg1.setText("Select Department to proceed");
           lbMsg1.setForeground(Color.red);
        }
    }//GEN-LAST:event_mnFoldersItemStateChanged

    private void mnSubFoldersItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_mnSubFoldersItemStateChanged
        // TODO add your handling code here:
        fileName.setText("");
        fileName.setEditable(false);
        cbFiles.removeAllItems();
        cbFiles.setEditable(false);
        txOwner.setText("");
        if(mnSubFolders.getItemCount()>0 && mnSubFolders!=null)
        {
        if(!mnSubFolders.getSelectedItem().equals("Select Folder"))
        {
            txOwner.setText(owners.get(mnSubFolders.getSelectedIndex()-1));
            if(typeUser.get(mnSubFolders.getSelectedIndex()-1).equals("Resident"))
            {
                cbFiles.setEditable(true);
                cbFiles.addItem("Select Type Of Document");
                for(List file : filesInfo)
                {
                    cbFiles.addItem((String) file.get(1));
                }
            }
            else
            {
                fileName.setText("Enter filename");
        fileName.setEditable(true); 
            }
              
        }
        else
        {
            txOwner.setText("");
        }
        }    
    }//GEN-LAST:event_mnSubFoldersItemStateChanged

    private void cbFilesItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbFilesItemStateChanged
        // TODO add your handling code here:
        int filename;
        filename=cbFiles.getSelectedIndex();
        if(!filesInfo.get(filename-1).isEmpty() && filename>0)
        {
            fileDescription.setText(String.valueOf(filesInfo.get(filename-1).get(2)));
        }
        else
        {
            fileDescription.setText("");
        }
    }//GEN-LAST:event_cbFilesItemStateChanged

    private void mnFilesItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_mnFilesItemStateChanged
        // TODO add your handling code here:
        int filename;
        filename=mnFiles.getSelectedIndex();
        if(!filesInfo.get(filename-1).isEmpty() && filename>0)
        {
            txtDescription.setText(String.valueOf(filesInfo.get(filename-1).get(2)));
        }
        else
        {
            txtDescription.setText("");
        }
    }//GEN-LAST:event_mnFilesItemStateChanged

    private void mnFilesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnFilesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mnFilesActionPerformed

    private void btnCreateFolderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateFolderActionPerformed
        // TODO add your handling code here:
    final String spreadsheetId = "1CHbGWR1L3KLxpSC6aYcAXTnS0cOeikMPq4jziT67RdY";
    final String range = "Administration_Information";
     GoogleCredential credential = null;
        try (InputStream credentialsStream = DASHBOARD.class.getResourceAsStream(CREDENTIALS_FILE_PATH_DRIVE)) {
            credential = GoogleCredential.fromStream(credentialsStream);
        } catch (IOException ex) {
          JOptionPane.showMessageDialog( null, ex);
      }
    credential = credential.createScoped(Collections.singleton(SheetsScopes.SPREADSHEETS));
    Sheets service;
      try {
         
         if(modelSetTitles.size()<1)
         {
             lbMsg2.setText("Granted access is empty");
             lbMsg2.setForeground(Color.red);
         }
         else
         {
             String grantedAccess="";
    for(int i=0;i<modelSetTitles.size();i++)
    {
        grantedAccess+=modelSetTitles.get(i)+";";
    }
    
    service = new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, credential)
                  .setApplicationName(APPLICATION_NAME)
                  .build();
          ValueRange response = service.spreadsheets().values()
        .get(spreadsheetId, range)
        .execute();
        List<List<Object>> values = response.getValues();
            String name=nmFolder.getText();
            String folderName=name.replace(" ","_");
            Date d=new Date();
            SimpleDateFormat dte = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");
            folderName=folderName+"_"+dte.format(d);
            String folderid=createFolder(folderName,"Administration");
            int ro=values.size();
            dte=new SimpleDateFormat("dd-MM-yyyy");
            ValueRange body = new ValueRange()
            .setValues(Arrays.asList(Arrays.asList(String.valueOf(ro),("FLD-"+String.valueOf(ro)),folderName,useremail,dte.format(d),folderid,grantedAccess,cboDepFolder.getSelectedItem())));
            // Write the data to the specified range in the spreadsheet
            service.spreadsheets().values()
            .update(spreadsheetId,"Administration_Information!A"+(ro+1)+":H"+(ro+1), body)
            .setValueInputOption("RAW")
            .execute();
            lbMsg2.setText("Folder "+folderName+" successfully created");
            lbMsg2.setForeground(Color.blue);
         }
      } catch (GeneralSecurityException | IOException ex) {
          JOptionPane.showMessageDialog(null, ex);
      } 
    
    }//GEN-LAST:event_btnCreateFolderActionPerformed

    private void btnDownloadFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDownloadFileActionPerformed
        // TODO add your handling code here:
        int row=tbFiles.getSelectedRow();
        downloadFile(row,"MINE");
        
    }//GEN-LAST:event_btnDownloadFileActionPerformed

    private void btnOtherReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOtherReportActionPerformed
        // TODO add your handling code here:
        workPlace.removeAll();
        tbFiles1.removeAll();
     workPlace.add(pnFilesAll);
     workPlace.setTitleAt(0, "LIST OF FILES IN A FOLDER");
     
     if(!title.equals("Administrator") && !title.equals("Director"))
     {
         cbDep1.removeAllItems();
         cbDep1.addItem("Select Department");
         cbDep1.addItem(department);
     }
    }//GEN-LAST:event_btnOtherReportActionPerformed

    private void btnDownloadFile1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDownloadFile1ActionPerformed
        // TODO add your handling code here:
        int row=tbFiles1.getSelectedRow();
        downloadFile(row,"ANY");     
    }//GEN-LAST:event_btnDownloadFile1ActionPerformed

    private void btnOtherArchiveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOtherArchiveActionPerformed
        // TODO add your handling code here:
     workPlace.removeAll();
     workPlace.add(pnFileFolder);
     workPlace.setTitleAt(0, "ADD FILE TO A FOLDER");
     if(!title.equals("Administrator") && !title.equals("Director"))
     {
         cbDep.removeAllItems();
         cbDep.addItem("Select Department");
         cbDep.addItem(department);
     }
    }//GEN-LAST:event_btnOtherArchiveActionPerformed

    private void cbArchiveItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbArchiveItemStateChanged
       if(cbDep1.getSelectedIndex()>0)
       {
           String dep=String.valueOf(cbDep1.getSelectedItem());
        cbFolder.removeAllItems();
        tbFiles1.removeAll();
        if(cbArchive.getSelectedItem().equals("Human Resource Archive"))
        {    
            try {  
                if(loadFolders("1vBhrfeof4YDKCrKB5DuITujQCfuRv3jw","User",dep))
                {
                cbFolder.setModel(modelSubFolders);
                }
            } catch (IOException | GeneralSecurityException ex) {
                JOptionPane.showMessageDialog(null, ex);
            } 
        }
        else if(cbArchive.getSelectedItem().equals("Administration Archive"))
        {
            try {  
                if(loadFolders("1F4i3S5iSRp55xiRwilMux_07-qonOh4T","Administration",dep));
                {
                cbFolder.setModel(modelSubFolders);
                }
                
            } catch (IOException | GeneralSecurityException ex) {
                JOptionPane.showMessageDialog(null, ex);
            }
        }
        else
        {
            mnSubFolders.removeAllItems();
        }
       }
       else
       {
         lbMsg5.setText("Select Department to proceed");
         lbMsg5.setForeground(Color.red);
       }
    }//GEN-LAST:event_cbArchiveItemStateChanged

    private void cbFolderItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbFolderItemStateChanged
        // TODO add your handling code here:
        tbFiles1.removeAll();
        if(!cbArchive.getSelectedItem().equals("Select Archive") && cbFolder.getItemCount()>0 && !cbFolder.getSelectedItem().equals("Select Folder"))
        {
         int row=cbFolder.getSelectedIndex()-1;
            try {
                loadFiles(SubFoldersId.get(row));
            } catch (IOException | GeneralSecurityException ex) {
                JOptionPane.showMessageDialog( null, ex);
            } 
            tbFiles1.setModel(model);
            
        
        }
    }//GEN-LAST:event_cbFolderItemStateChanged

    private void btnPortofolio1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPortofolio1ActionPerformed
        // TODO add your handling code here:
        workPlace.removeAll();
        try
        {
            if("Resident".equals(title))
            {
      loadFilesType();
      nmFiles.setEditable(false);
      txtDescription.setEditable(false);
            }
            else
            {
                mnFiles.setEditable(false);
                nmFiles.setEditable(true);
                txtDescription.setEditable(true);
            }
        }
      catch(IOException | GeneralSecurityException e)
        {
      JOptionPane.showMessageDialog(null, e);
        }
        
     workPlace.add(pnAddFileToMyFolder);
     workPlace.setTitleAt(0, "ADD FILE TO MY FOLDER");
    }//GEN-LAST:event_btnPortofolio1ActionPerformed

    private void btnesignActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnesignActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnesignActionPerformed

    private void btnuserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnuserActionPerformed
        // TODO add your handling code here:
        Date d=new Date();
        SimpleDateFormat dte=new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss_SSS");
        String folderName=txname.getText()+"_"+txlastname.getText()+"_"+dte.format(d);
        folderName=folderName.replace(" ", "_");
      try {
          String id=createFolder(folderName,"User");
          final String spreadsheetId = "1CHbGWR1L3KLxpSC6aYcAXTnS0cOeikMPq4jziT67RdY";
    final String range = "Basic_Information";
     GoogleCredential credential = null;
        try (InputStream credentialsStream = DASHBOARD.class.getResourceAsStream(CREDENTIALS_FILE_PATH_DRIVE)) {
            credential = GoogleCredential.fromStream(credentialsStream);
        } catch (IOException ex) {
          JOptionPane.showMessageDialog( null, ex);
      }
    credential = credential.createScoped(Collections.singleton(SheetsScopes.SPREADSHEETS));
    Sheets service;
      try {
          service = new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, credential)
                  .setApplicationName(APPLICATION_NAME)
                  .build();
          ValueRange response = service.spreadsheets().values()
        .get(spreadsheetId, range)
        .execute();
        List<List<Object>> values = response.getValues();
        int ro=values.size();
        dte=new SimpleDateFormat("ddMMyyyy");
        String code="";
        
        code=dte.format(d)+"0"+String.valueOf(cbdep.getSelectedIndex())+ro;
        
        String pass=generateRandomPassword(6);
        dte=new SimpleDateFormat("dd/MM/yyyy");
        ValueRange body = new ValueRange()
        .setValues(Arrays.asList(Arrays.asList(String.valueOf(ro),
        code,
        txname.getText()+" "+txlastname.getText(),
        String.valueOf(cbsex.getSelectedItem()),
        txtitle.getText(),
        String.valueOf(cbdep.getSelectedItem()),txemail.getText(),
        txphone.getText(),dte.format(txdate.getDate()),usercode,
        "NONE",id,String.valueOf(cbfunct.getSelectedItem()),
        pass,"YES","NONE",dte.format(d))));
            // Write the data to the specified range in the spreadsheet
              service.spreadsheets().values()
            .update(spreadsheetId,"Basic_Information!A"+(ro+1)+":Q"+(ro+1), body)
            .setValueInputOption("RAW")
            .execute();
              String content="<p>Dear "+txname.getText()+" "+txlastname.getText()+";"
                      +"<br> You are registered on RIIO APP with following credentials</p>"
                      + "<table><tr><td colspan='2'>USER CREDENTIALS</td></tr>"
                      + "<tr><td>Username:</td><td>"+txemail.getText()+"</td></tr>"
                      + "<tr><td>Password:</td><td>"+pass+"</td><tr></table><p></br></br></p>"
                      + "<p style='font-weight:bold;'>This email is auto generated do not reply to it.</p>"
                      + "<p>**********************</p>";
            String mail=SendEmail.sendEmail(txemail.getText(), "RIIO APP REGISTRATION", content);
            lbMsg6.setText("Staff "+folderName+" successfully created and "+mail);
            lbMsg6.setForeground(Color.blue);
      }
      catch (GeneralSecurityException | IOException ex) {
          JOptionPane.showMessageDialog(null, ex);
      }
          
      } catch (IOException ex) {
          JOptionPane.showMessageDialog(null, ex);
      }
        
    }//GEN-LAST:event_btnuserActionPerformed

    private void btnphotoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnphotoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnphotoActionPerformed

    private void btnOtherArchive1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOtherArchive1ActionPerformed
        // TODO add your handling code here:
     workPlace.removeAll();
     workPlace.add(pnAddNewUser);
     workPlace.setTitleAt(0, "ADD NEW STAFF");
    }//GEN-LAST:event_btnOtherArchive1ActionPerformed

    private void txtitleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtitleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtitleActionPerformed

    private void cbArchiveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbArchiveActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbArchiveActionPerformed

    private void nmFolderMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_nmFolderMouseClicked
        // TODO add your handling code here:
        if(nmFolder.getText().equals("Enter Folder Name"))
        {
            nmFolder.setText("");
        }
    }//GEN-LAST:event_nmFolderMouseClicked

    private void txOwnerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txOwnerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txOwnerActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        if(!jlTitles.isSelectionEmpty())
        {
            
            modelSetTitles.addElement(String.valueOf(jlTitles.getSelectedValue()));
            modelTitles.removeElement(String.valueOf(jlTitles.getSelectedValue()));
            jlTitles.setModel(modelTitles);
            jlGrantedTitles.setModel(modelSetTitles);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void cboDepFolderItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboDepFolderItemStateChanged
        // TODO add your handling code here:
         jlTitles.removeAll(); 
         jlGrantedTitles.removeAll(); 
        if(cboDepFolder.getSelectedIndex()>0)
        {
          String dep=String.valueOf(cboDepFolder.getSelectedItem());
            try {
                Set titles=titles(dep);
                if(!titles.isEmpty())
                {
                 modelTitles=new DefaultListModel();
                 modelSetTitles=new DefaultListModel();
                 for(Object elem : titles)
                 {
                 modelTitles.addElement(String.valueOf(elem));
                 }
                 jlTitles.setModel(modelTitles);
                }
                
            } catch (IOException | GeneralSecurityException ex) {
                JOptionPane.showMessageDialog(null, ex);
            } 
        }
    }//GEN-LAST:event_cboDepFolderItemStateChanged

    private void jlTitlesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jlTitlesMouseClicked
        // TODO add your handling code here:
        
    }//GEN-LAST:event_jlTitlesMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DASHBOARD.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DASHBOARD.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DASHBOARD.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DASHBOARD.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DASHBOARD().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bntViewFile;
    private javax.swing.JButton bntViewFile1;
    private javax.swing.JButton btnBrowse;
    private javax.swing.JButton btnBrowse1;
    private javax.swing.JButton btnCreateFolder;
    private javax.swing.JButton btnCreateFolderArchive;
    private javax.swing.JButton btnDashboard;
    private javax.swing.JButton btnDownloadFile;
    private javax.swing.JButton btnDownloadFile1;
    private javax.swing.JButton btnMyReport;
    private javax.swing.JButton btnOtherArchive;
    private javax.swing.JButton btnOtherArchive1;
    private javax.swing.JButton btnOtherReport;
    private javax.swing.JButton btnPortofolio1;
    private javax.swing.JButton btnUploadPortofolioDoc;
    private javax.swing.JButton btnUploadPortofolioDoc1;
    private javax.swing.JButton btnesign;
    private javax.swing.JButton btnphoto;
    private javax.swing.JButton btnuser;
    private javax.swing.JComboBox<String> cbArchive;
    private javax.swing.JComboBox<String> cbDep;
    private javax.swing.JComboBox<String> cbDep1;
    private javax.swing.JComboBox<String> cbFiles;
    private javax.swing.JComboBox<String> cbFolder;
    private javax.swing.JComboBox<String> cbdep;
    private javax.swing.JComboBox<String> cbfunct;
    private javax.swing.JComboBox<String> cboDepFolder;
    private javax.swing.JComboBox<String> cbsex;
    private javax.swing.JTextArea fileDescription;
    private javax.swing.JTextField fileName;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JList<String> jlGrantedTitles;
    private javax.swing.JList<String> jlTitles;
    private javax.swing.JLabel lbDep;
    private javax.swing.JLabel lbEmail;
    private javax.swing.JLabel lbFile;
    private javax.swing.JLabel lbFile1;
    private javax.swing.JLabel lbMsg;
    private javax.swing.JLabel lbMsg1;
    private javax.swing.JLabel lbMsg2;
    private javax.swing.JLabel lbMsg4;
    private javax.swing.JLabel lbMsg5;
    private javax.swing.JLabel lbMsg6;
    private javax.swing.JLabel lbTitle;
    private javax.swing.JLabel lbUser;
    private javax.swing.JLabel lbimage;
    private javax.swing.JLabel lblogo;
    private javax.swing.JComboBox<String> mnFiles;
    private javax.swing.JComboBox<String> mnFolders;
    private javax.swing.JComboBox<String> mnSubFolders;
    private javax.swing.JTextField nmFiles;
    private javax.swing.JTextField nmFolder;
    private javax.swing.JPanel pnAccess;
    private javax.swing.JPanel pnAddFileToMyFolder;
    private javax.swing.JPanel pnAddNewUser;
    private javax.swing.JPanel pnCreateFolderToArchive;
    private javax.swing.JPanel pnDashboard;
    private javax.swing.JPanel pnEveryone;
    private javax.swing.JPanel pnFileFolder;
    private javax.swing.JPanel pnFileToFolder;
    private javax.swing.JPanel pnFileToMyArchive;
    private javax.swing.JPanel pnFilesAll;
    private javax.swing.JPanel pnHigh;
    private javax.swing.JPanel pnMyFiles;
    private javax.swing.JTable tbFiles;
    private javax.swing.JTable tbFiles1;
    private javax.swing.JTextField txOwner;
    private com.toedter.calendar.JDateChooser txdate;
    private javax.swing.JTextField txemail;
    private javax.swing.JTextField txlastname;
    private javax.swing.JTextField txname;
    private javax.swing.JTextField txphone;
    private javax.swing.JTextArea txtDescription;
    private javax.swing.JTextField txtitle;
    private javax.swing.JTabbedPane workPlace;
    // End of variables declaration//GEN-END:variables
}
