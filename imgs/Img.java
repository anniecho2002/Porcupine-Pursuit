package imgs;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
public enum Img{
  home_background0, home_background1, title, blackBackground,
  heart, heartMessage, 
  orange, orangeScream, 
  cloud0, cloud1,
  levelone, leveltwo,
  grass0, grass1, grass2, grass3, grass4, 
  bush0, bush1, bush2, bush3, bush4,
  foxIdle0, foxIdle1, foxIdle2, foxIdle3, foxIdle4,
  foxRun0, foxRun1, foxRun2, foxRun3, foxRun4, foxRun5, foxRun6, foxRun7,
  foxLook0, foxLook1, foxLook2, foxLook3, foxLook4, foxLook5, foxLook6, 
  foxLook7, foxLook8, foxLook9, foxLook10, foxLook11, foxLook12, foxLook13,
  porcWalk0, porcWalk1, porcWalk2, porcWalk3, porcWalk4,
  porcSpike0, porcSpike1, porcSpike2, porcSpike3, porcSpike4, porcSpike5, porcSpike6, porcSpike7,
  porcSleep0, porcSleep1, porcSleep2, porcSleep3, porcSleep4;
  
  public final BufferedImage image;
  
  Img(){
    image = loadImage(this.name());
  }
  
  static private BufferedImage loadImage(String name){
    URL imagePath = Img.class.getResource(name + ".png");
    try{ 
      return ImageIO.read(imagePath);
    } catch(IOException e) { 
      throw new Error(e); 
    }
  }
  
}