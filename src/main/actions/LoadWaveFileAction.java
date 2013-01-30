package main.actions;

import java.io.File;

import javax.swing.JFileChooser;

import main.AnimatedFace;
import main.sound.wave.WaveFileFilter;


public class LoadWaveFileAction extends ControlAction{

	private JFileChooser chooser;
	private String lastDirectory;
	private WaveFileFilter filter;

	public LoadWaveFileAction() {
		super("Load Wave File");
		lastDirectory="wavefiles";
		filter=new WaveFileFilter();
	}
	
	@Override
	protected void actionPerformed() {
		chooser=new JFileChooser(lastDirectory);
		chooser.setFileFilter(filter);
		chooser.showOpenDialog(AnimatedFace.window);
		
		try {
			File f=chooser.getSelectedFile();
			if(f.getName().toLowerCase().endsWith(".wav")){
				AnimatedFace.setWaveFile(f);
			}
		} catch (Exception e) {
		}
	}


}
