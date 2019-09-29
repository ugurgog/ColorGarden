package uk.co.senab.photoview;

public interface OnDrawLineListener {
    void OnDrawFinishedListener(boolean drawed, int startX, int startY, int endX, int endY);

    void OnGivenFirstPointListener(int startX, int startY);

    void OnGivenNextPointListener(int endX, int endY);
}
