package image;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.*;

/**
 * A simple display for images using JFrame.
 * @author �rick Oliveira Rodrigues (erickr@id.uff.br)
 */
public class ImageDisplay extends JFrame{

	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Panel panel = new Panel(this);
	private String title = "";
	
	ImageDisplay(){
		this.setLayout(new BorderLayout());
	    this.add(panel, BorderLayout.CENTER);
	    this.setSize(500, 400);
	    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	public void setTitle(final String title){this.title = title; super.setTitle(title);}
	public void appendTitle(final String title){super.setTitle(this.title + title);}
	public void setImage(BufferedImage img){panel.setImage(img); this.setVisible(true);}
	public void setImage(Image img) throws Exception{
		
		double width = screenSize.getWidth();
		double height = screenSize.getHeight();
		
		panel.setBufferedImage(img);
		if (img.getHeight() < height/2 && img.getWidth() < width/2)
			if (img.getWidth() < 200 && img.getHeight() < 100) {
				this.setSize(200 + 200, 100);
			}
			else {
				this.setSize(img.getWidth(), img.getHeight());
			}
		else{
			int x = img.getWidth()/2; int y = img.getHeight()/2;
			while (x > width/2 || y > height/2){
				x /= 2;
				y /= 2;
			}
			this.setSize(x, y);
		}
		this.setVisible(true);
	}
	
	public class Panel extends JPanel{
		private static final long serialVersionUID = 1L;
		private BufferedImage bufferedImage = null;
		private ImageDisplay frame = null;
		private Image image = null;

		Panel(ImageDisplay frame) {
			this.frame = frame;
			this.setOpaque(false);


			//mouse click
			MouseListener ml = new MouseListener(){
				@Override
				public void mouseClicked(MouseEvent e) {
					if (image == null) return;

					// parent component of the dialog
					JFrame parentFrame = new JFrame();

					JFileChooser fileChooser = new JFileChooser();
					fileChooser.setDialogTitle("SAVE AS: Choose an image name and path (including extension)");


					int userSelection = fileChooser.showSaveDialog(parentFrame);
					try {
						String p = fileChooser.getSelectedFile().getPath();
						if (!p.contains("."))
							p += ".png";

						image.exportImage(p);
					} catch (Exception ex) {
						//ex.printStackTrace();
						System.out.println("Image has not been saved...");
					}

				}

				@Override
				public void mousePressed(MouseEvent e) {

				}

				@Override
				public void mouseReleased(MouseEvent e) {

				}

				@Override
				public void mouseEntered(MouseEvent e) {

				}

				@Override
				public void mouseExited(MouseEvent e) {

				}
			};
			this.addMouseListener(ml);

		}



		/*
	    public ImageDisplay(BufferedImage img) {
	       try {                
	          image = ImageIO.read(new File("image name and path"));
	       } catch (IOException ex) {
	            // handle exception...
	       }
	    }*/
		public void setBufferedImage(Image img) throws Exception{
			this.image = img;
			this.bufferedImage = img.getBufferedImage();
			this.frame.appendTitle(" (" + img.getWidth() + " x " + img.getHeight() + ") - N. of Bands: " + img.getNumBands() + " - Min Value (band 0): " + img.getMinimalIntesity(0) + " - Max Value (band 0): " + img.getMaximalIntensity(0));
			if (this.getGraphics() != null) this.paintComponent(this.getGraphics());
		}
		public void setImage(BufferedImage img){
			this.image = new Image(img);
			this.bufferedImage = img;
			this.frame.appendTitle(" (" + img.getWidth() + " x " + img.getHeight() + ")");
			if (this.getGraphics() != null) this.paintComponent(this.getGraphics());
		}
	
	    @Override
	    protected void paintComponent(Graphics g) {
	    	Graphics2D g2 = (Graphics2D) g;
	    	float rX = (frame.getWidth() - 16)/(float) bufferedImage.getWidth(), rY = (frame.getHeight() - 39)/(float) bufferedImage.getHeight();
	    	g2.scale(rX, rY);
	        super.paintComponent(g2);
	        g2.drawImage(bufferedImage, 0, 0, null); // see javadoc for more info on the parameters
	    }
	}

}