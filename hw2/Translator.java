public class Translator{
	//translate short data to byte and store in byte array
	public void toBytes(byte[] header, int start, short data){
		//byte[] tmp = new byte[2];
		header[start] = (byte) (data & 0xFF);
		header[start+1] = (byte) ((data >> 8) & 0xFF);
	}
	//translate int data to byte and store in byte array
	public void toBytes(byte[] header, int start, int data){
		header[start] = (byte) (data & 0xFF);
		header[start + 1] = (byte) ((data >> 8) & 0xFF);
		header[start + 2] = (byte) ((data >> 16) & 0xFF);
		header[start + 3] = (byte) ((data >> 24) & 0xFF);
	}
	//translate byte array to int
	public int toInt(byte[] header, int start){
		int tmp = (header[start] & 0xFF) | ((header[start + 1] << 8) & 0xFF00) |
					((header[start + 2] << 16) & 0xFF0000) | ((header[start + 3] << 24) & 0xFF000000);
		return tmp;
	}

}