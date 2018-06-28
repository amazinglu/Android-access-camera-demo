# Android-access-camera-demo

allows the app to use the system's camera to take photos, store the photo to external sotrage and be showed in the Gallery
fixs the problem of FileUriExposedException at SDK above 24 when file:// is attached with Intent
  - file:// is not allowed to attach with Intent anymore. 
  - send the URI through content:// scheme instead which is the URI scheme for Content Provider
  
reference: https://inthecheesefactory.com/blog/how-to-share-access-to-file-with-fileprovider-on-android-nougat/en
