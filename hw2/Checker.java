import java.util.Arrays;

public class Checker {

  public long Checksum(byte[] buf, int start,int totallength) {
    int length = totallength - start;
    int i = start;

    long sum = 0;
    long data;

    // Handle all pairs
    while (length > 1) {
      // Corrected to include @Andy's edits and various comments on Stack Overflow
      data = (((buf[i] << 8) & 0xFF00) | ((buf[i + 1]) & 0xFF));
      sum += data;
      // 1's complement carry bit correction in 16-bits (detecting sign extension)
      if ((sum & 0xFFFF0000) > 0) {
        sum = sum & 0xFFFF;
        sum += 1;
      }

      i += 2;
      length -= 2;
    }

    // Handle remaining byte in odd length buffers
    if (length > 0) {
      // Corrected to include @Andy's edits and various comments on Stack Overflow
      sum += (buf[i] << 8 & 0xFF00);
      // 1's complement carry bit correction in 16-bits (detecting sign extension)
      if ((sum & 0xFFFF0000) > 0) {
        sum = sum & 0xFFFF;
        sum += 1;
      }
    }

    // Final 1's complement value correction to 16-bits
    sum = ~sum;
    sum = sum & 0xFFFF;
    return sum;

  }

  public boolean CompareChecksum(short checksum, byte[] segment, int start){
    byte[] tmp = new byte[2];
    tmp[0] = (byte)(checksum & 0xFF);
    tmp[1] = (byte)((checksum >> 8) & 0xFF);
    if((tmp[0] == segment[start]) && (tmp[1] == segment[start + 1]))
      return true;
    else
      return false;
  }

  public boolean CompareACK(int ack, byte[] segment, int start){
    Translator translator = new Translator();
    int seq = translator.toInt(segment, start);
    if(ack == seq)
      return true;
    else
      return false;
  }

}