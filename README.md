# Android-access-camera-demo


1. create a file to store the image that the camera taken

    createImageFile()

2. send the uri of this file to camera app to tell the app to put the image into this path

    cannot directly attach the file path to intent directly
    use FileProvider (Content provider)

    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
    startActivityForResult(intent, REQ_CODE_TAKE_PICTURE);

3. get the bitmap out from the file and set imageView

    onActivityResult()
