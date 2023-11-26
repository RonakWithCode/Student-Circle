package com.crazyostudio.studentcircle.model;

import android.net.Uri;

public interface ChatAdaptersInterface {
    void Delete(Chat_Model chatModel);
    void ReceiveDelete(Chat_Model chatModel);
    void DeleteImage(Chat_Model chatModel);
    void ImageView(Chat_Model chatModel);
    void ReceiveDeleteImage(Chat_Model chatModel);
    void Download(Uri uri);

}
