package main.perceptiontest.video;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.FileWriter;

import javax.media.MediaLocator;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.sf.fmj.ui.application.CaptureDeviceBrowser;
import net.sf.fmj.ui.application.ContainerPlayer;
import net.sf.fmj.ui.application.ContainerPlayerStatusListener;
import net.sf.fmj.ui.application.PlayerPanelPrefs;
import net.sf.fmj.ui.control.TransportControlPanel;
import net.sf.fmj.ui.dialogs.RTPReceivePanel;
import net.sf.fmj.ui.dialogs.URLPanel;
import net.sf.fmj.ui.wizards.RTPTransmitWizard;
import net.sf.fmj.ui.wizards.TranscodeWizard;
import net.sf.fmj.utility.URLUtils;


public class PlayerPanel extends JPanel {

	private PlayerPanelPrefs prefs;
	private JToolBar playerToolBar = null;
	private JButton openButton = null;
	private JButton openCaptureDeviceButton = null;
	private TransportControlPanel transportControlPanel = null;
	private JPanel videoPanel = null;
	private JComboBox addressComboBox = null;
	private JButton loadButton = null;
	private JPanel addressPanel = null;
	private JLabel locationLabel = null;
	private ContainerPlayer containerPlayer = null;  //  @jve:decl-index=0:visual-constraint="557,162"
	private String file, dir;
	private long fileSize;
	private JButton playButton;


	public void addMediaLocatorAndLoad(String url) {
		boolean alreadyThere = false;
		for (int i = 0; i < getAddressComboBox().getItemCount(); ++i) {
			if (getAddressComboBox().getItemAt(i).equals(url)) {
				alreadyThere = true;
				break;
			}
		}
		if (!alreadyThere) {
			getAddressComboBox().addItem(url);
		}


		if (getAddressComboBox().getSelectedItem() == null || !getAddressComboBox().getSelectedItem().equals(url)) {
			getAddressComboBox().setSelectedItem(url);  // will auto-load
		} else {
			onLoadButtonClick();        // already selected
		}
	}
	
	/**
	 * This method initializes videoPanel
	 *
	 * @return javax.swing.JPanel
	 */
	public JPanel getVideoPanel() {
		if (videoPanel == null) {
			videoPanel = new JPanel();
            videoPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
			videoPanel.setBackground(SystemColor.BLACK);
		}
		return videoPanel;
	}


	/**
	 * This method initializes addressComboBox
	 *
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getAddressComboBox() {
		if (addressComboBox == null) {
			addressComboBox = new JComboBox();
			addressComboBox.setEditable(true);
			addressComboBox.setPreferredSize(new Dimension(160, 27));
			for (int i = 0; i < prefs.recentUrls.size(); i++) {
				addressComboBox.addItem((String) prefs.recentUrls.get(i));
			}
			addressComboBox.setSelectedIndex(-1);       // nothing selected by default.


			// load an item when selected from the list
			addressComboBox.addItemListener(
					new ItemListener() {


						public void itemStateChanged(ItemEvent e) {
							if (e.getStateChange() == ItemEvent.SELECTED) {
								onLoadButtonClick();
							}
						}
					});
		}
		return addressComboBox;
	}

	public void loadMedia(String location,boolean autoplay){
		setCursor(new Cursor(Cursor.WAIT_CURSOR));


		try {

			getContainerPlayer().setMediaLocation(location, autoplay);
		} catch (Throwable e) {
			return;
		} finally {
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
	}
	
	public void startMedia(){
		try {
			getContainerPlayer().setPosition(0.0);
			getContainerPlayer().start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void onLoadButtonClick() {


		String location = (String) getAddressComboBox().getSelectedItem();


		if (location.trim().equals("")) {
			return;
		}


		setCursor(new Cursor(Cursor.WAIT_CURSOR));


		try {

			getContainerPlayer().setMediaLocation(location, prefs.autoPlay);
		} catch (Throwable e) {
			return;
		} finally {
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}


		// update prefs with new URL
		if (!prefs.recentUrls.contains(location)) {
			prefs.recentUrls.add(0, location);
			savePrefs();
		} else {
			// in list, make sure it is first.
			if (!prefs.recentUrls.get(0).equals(location)) {
				// not in first position.  remove and re-add at head.
				prefs.recentUrls.remove(location);
				prefs.recentUrls.add(0, location);
				savePrefs();
			}
		}
	}


	/**
	 * This method initializes containerPlayer
	 *
	 * @return net.sf.fmj.ui.application.ContainerPlayer
	 */
	public ContainerPlayer getContainerPlayer() {
		if (containerPlayer == null) {
			containerPlayer = new ContainerPlayer(getVideoPanel());
			// containerPlayer.setAutoLoop(prefs.autoLoop);
			containerPlayer.setContainerPlayerStatusListener(new ContainerPlayerStatusListener() {


				public void onStatusChange(final String newStatus) {
					SwingUtilities.invokeLater(new Runnable() {


						public void run() {
							containerPlayer.getTime();
						}
					});


				}
			});
		}
		return containerPlayer;
	}


	/**
	 * This method initializes
	 *
	 */
	public PlayerPanel() {
		super();
		initialize();
	}


	/**
	 * This method initializes this
	 *
	 */
	private void initialize() {

		this.setLayout(new BorderLayout());

		this.add(getVideoPanel(), BorderLayout.CENTER);
		this.add(getPlayerToolBar(),BorderLayout.NORTH);

	}



	/** save preferences. */
	private void savePrefs() {
		try {
			FileWriter fileWriter = new FileWriter(PlayerPanelPrefs.getFile());
			prefs.write(fileWriter);
			fileWriter.flush();
			fileWriter.close();
		} catch (Exception e) {
		}
	}


	/**
	 * This method initializes playerToolBar
	 *
	 * @return javax.swing.JToolBar
	 */
	private JToolBar getPlayerToolBar() {
		if (playerToolBar == null) {
			playerToolBar = new JToolBar();
			playerToolBar.setFloatable(false);
			//playerToolBar.add(getOpenButton());
			//playerToolBar.add(getOpenCaptureDeviceButton());
			//playerToolBar.add(getAddressPanel());
			//playerToolBar.add(getLoadButton());
		}
		return playerToolBar;
	}


	/**
	 * This method initializes openButton
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getOpenButton() {
		if (openButton == null) {
			openButton = new JButton();
			openButton.setToolTipText("Abrir Vídeo");
			openButton.setIcon(new ImageIcon(getClass().getResource("/net/sf/fmj/ui/images/cvs_folder_rep.png")));
			openButton.addActionListener(new ActionListener() {


				public void actionPerformed(ActionEvent arg0) {
					//onOpenFile();
				}
			});
		}
		return openButton;
	}


	public void onOpenFile() {


		final JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(new FileNameExtensionFilter("Videodatei", "avi"));
		chooser.setAcceptAllFileFilterUsed(false);
		if (chooser.showOpenDialog(PlayerPanel.this) == JFileChooser.APPROVE_OPTION) {
			final String urlStr = URLUtils.createUrlStr(chooser.getSelectedFile());


			setDir(chooser.getCurrentDirectory().toString() + java.io.File.separator);
			setFile(chooser.getSelectedFile().getName());



			fileSize = chooser.getSelectedFile().length();
			addMediaLocatorAndLoad(urlStr);


		}
	}


	
	public void onOpenCaptureDevice() {
		MediaLocator locator = CaptureDeviceBrowser.run(getParentFrame());
		if (locator != null) {
			addMediaLocatorAndLoad(locator.toExternalForm());
		}
	}


	private Frame getParentFrame() {
		Container c = getParent();
		while (c != null) {
			if (c instanceof Frame) {
				return (Frame) c;
			}


			c = c.getParent();
		}
		throw new RuntimeException("No parent frame");
	}


	public void onTransmitRTP() {
		RTPTransmitWizard w = new RTPTransmitWizard(getParentFrame(), prefs.rtpTransmitWizardConfig);
		boolean result = w.run();


		//  store preferences if successful
		if (result) {
			getContainerPlayer().setRealizedStartedProcessor(w.getResult().processor);


			prefs.rtpTransmitWizardConfig = w.getConfig(); 
			savePrefs();
		}


	}


	public void onTranscode() {
		TranscodeWizard w = new TranscodeWizard(getParentFrame(), prefs.transcodeWizardConfig);
		boolean result = w.run();


		//  store preferences if successful
		if (result) {
			getContainerPlayer().setRealizedStartedProcessor(w.getResult().processor);


			prefs.transcodeWizardConfig = w.getConfig();   
			savePrefs();
		}
	}


	public void onReceiveRTP() {
		String url = RTPReceivePanel.run(getParentFrame());
		if (url == null) {
			return;     // cancel
		}
		addMediaLocatorAndLoad(url);
	}


	public void onOpenURL() {
		String url = URLPanel.run(getParentFrame());
		if (url == null) {
			return;     // cancel
		}
		addMediaLocatorAndLoad(url);
	}


	public void onAutoPlay(boolean value) {
		prefs.autoPlay = value;
		savePrefs();
	}


	public void onAutoLoop(boolean value) {
		prefs.autoLoop = false;
		if (getContainerPlayer() != null) {
			getContainerPlayer().setAutoLoop(value);
		}


		savePrefs();
	}


	public PlayerPanelPrefs getPrefs() {
		return prefs;
	}


	/**
	 * This method initializes transportControlPanel
	 *
	 * @return net.sf.fmj.ui.control.TransportControlPanel
	 */
	public TransportControlPanel getTransportControlPanel() {
		if (transportControlPanel == null) {
			transportControlPanel = new TransportControlPanel();
			transportControlPanel.setPlayer(getContainerPlayer());
		}
		return transportControlPanel;
	}


	/**
	 * @return the file
	 */
	public String getFile() {
		return file;
	}


	public void setFile(String file) {
		this.file = file;
	}


	public String getDir() {
		return dir;
	}


	public void setDir(String dir) {
		this.dir = dir;
	}


	public long getFileSize() {
		return fileSize;
	}

	public void setPlayButton(JButton playButton) {
		this.playButton=playButton;
	}
}