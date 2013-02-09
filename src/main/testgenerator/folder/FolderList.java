package main.testgenerator.folder;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Vector;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import main.actions.AddStimuliFolderAction;
import main.actions.RemoveStimuliFolderAction;
import main.testgenerator.file.FileWithSentence;
import main.xml.XmlFactory;

public class FolderList extends Container {

	private DefaultMutableTreeNode selectRoot,usedFolderRoot;
	private JTree selectTree,usedFolderTree;
	protected String currentDirectoryPath="";
	private StimuliFileFilter filter;
	private Container center,north,south;
	private DefaultTreeModel usedFolderModel;
	
	private final String SENTENCE="sentence",WORD="word";

	public FolderList() {
		setLayout(new BorderLayout());

		center=new Container();
		center.setLayout(new GridLayout(1,2,10,10));

		north=new Container();
		north.setLayout(new FlowLayout(FlowLayout.CENTER,20,5));

		south=new Container();
		south.setLayout(new FlowLayout(FlowLayout.CENTER,20,5));

		filter=new StimuliFileFilter();

		selectRoot=new DefaultMutableTreeNode("Verfügbare Ordner mit Stimuli");
		selectTree=new JTree(selectRoot);
		selectTree.setAutoscrolls(true);
		selectTree.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {}

			@Override
			public void mousePressed(MouseEvent e) {}

			@Override
			public void mouseExited(MouseEvent e) {}

			@Override
			public void mouseEntered(MouseEvent e) {}

			@Override
			public void mouseClicked(MouseEvent e) {
				selectTree.setSelectionRow(selectTree.getRowForLocation(e.getX(), e.getY()));
				try {
					if(e.getButton()==MouseEvent.BUTTON3)
					{
						DefaultMutableTreeNode node=(DefaultMutableTreeNode)selectTree.
								getPathForRow(selectTree.getMaxSelectionRow()).
								getLastPathComponent();

						System.out.println(node);

						if(node.getChildCount()>0)
						{
							Object nodeElement=node.getUserObject();

							if(nodeElement instanceof StimuliFolder)
							{
								JPopupMenu contextMenu=new JPopupMenu();
								contextMenu.add(new JMenuItem(new AddStimuliFolderAction((StimuliFolder)nodeElement)));
								contextMenu.show(selectTree, e.getX(), e.getY());
							}
						}
					}
				} catch (NullPointerException e1) {
				}
			}
		});

		center.add(new JScrollPane(selectTree));

		try {
			addFolder(new File("audio"), selectRoot);
			addFolder(new File("videos"), selectRoot);
			selectTree.expandRow(0);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		/////////////////////

		usedFolderRoot=new DefaultMutableTreeNode("Verwendete Ordner mit Stimuli");
		usedFolderModel=new DefaultTreeModel(usedFolderRoot);
		usedFolderTree=new JTree(usedFolderModel);
		usedFolderTree.setAutoscrolls(true);
		usedFolderTree.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {}

			@Override
			public void mousePressed(MouseEvent e) {}

			@Override
			public void mouseExited(MouseEvent e) {}

			@Override
			public void mouseEntered(MouseEvent e) {}

			@Override
			public void mouseClicked(MouseEvent e) {
				usedFolderTree.setSelectionRow(usedFolderTree.getRowForLocation(e.getX(), e.getY()));
				try {
					if(e.getButton()==MouseEvent.BUTTON3)
					{
						DefaultMutableTreeNode node=(DefaultMutableTreeNode)selectTree.
								getPathForRow(selectTree.getMaxSelectionRow()).
								getLastPathComponent();

						Object nodeElement=node.getUserObject();

						if(nodeElement instanceof StimuliFolder)
						{
							JPopupMenu contextMenu=new JPopupMenu();
							contextMenu.add(new JMenuItem(new RemoveStimuliFolderAction()));
							contextMenu.show(usedFolderTree, e.getX(), e.getY());
						}

					}
				} catch (NullPointerException e1) {
					e1.printStackTrace();
				}
			}
		});

		center.add(new JScrollPane(usedFolderTree));

		/////////////////////

		add(north,BorderLayout.NORTH);
		add(center,BorderLayout.CENTER);
		add(south,BorderLayout.SOUTH);
	}

	private void addFolder(File folder, DefaultMutableTreeNode parent) 
			throws NullPointerException {
		DefaultMutableTreeNode foldernode=new DefaultMutableTreeNode(new StimuliFolder(folder.getAbsolutePath()));
		
		boolean addLeaves=checkFolderForSentenceFile(folder,foldernode); 

		for(File file:folder.listFiles())
		{
			if(filter.accept(file))
			{
				DefaultMutableTreeNode child;

				if(file.isDirectory())
				{
					child=new DefaultMutableTreeNode(new StimuliFolder(file.getAbsolutePath()));
					addFolder(file, foldernode);
				}
				else
				{
					if(addLeaves)
					{
						child=new DefaultMutableTreeNode(file.getName());
						foldernode.add(child);
					}
				}
			}
		}

		parent.add(foldernode);
	}

	private boolean checkFolderForSentenceFile(File folder, DefaultMutableTreeNode node) {
		
		StimuliFolder stimFolder;
		try {
			stimFolder = (StimuliFolder)node.getUserObject();
		} catch (Exception e) {
			return false;
		}
		
		try {
			if(folder.isDirectory())
			{
				for(String name:folder.list())
				{
					if(name.toLowerCase().matches("sentence\\.xml"))
					{
//						System.out.println("\nsentence.xml in "+folder.getAbsolutePath());
						
						BufferedReader reader=new BufferedReader(
								new FileReader(
										new File(
												folder.getAbsolutePath()+
												File.separator+"sentence.xml")));
						
						String content="",line="";
						while((line=reader.readLine())!=null)
						{
							line=line.replaceAll("\\\t", "");
							content+=line.trim();
						}
						reader.close();
						
						for(String tag:content.split("><"))
						{
							tag=tag.trim();

							if(tag.length()>0 && !tag.startsWith("!--"))
							{
								try {
									String[] parts=tag.split("[><]");
									if(parts[0].trim().matches(WORD))
									{
										if(parts[2].trim().matches(XmlFactory.getCloseTag(WORD)))
										{
											stimFolder.addWordToSentence(parts[1]);
										}
									}
								} catch (Exception e) {
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
//		System.out.println(stimFolder.countWords());
		
		return stimFolder.countWords()>0;
	}

	public void addUsedFolder(StimuliFolder folder) {
		try {
			addFolder(folder.toFile(), usedFolderRoot);
			usedFolderModel.reload();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

	}

	public void removeSelectedUsedFolder() {
		DefaultMutableTreeNode node;
		TreePath[] paths = usedFolderTree.getSelectionPaths();
		for (int i = 0; i < paths.length; i++) {
			node = (DefaultMutableTreeNode) (paths[i].getLastPathComponent());
			usedFolderModel.removeNodeFromParent(node);
		}
	}

	public Vector<FileWithSentence> listAllUsedFiles() {
		Vector<FileWithSentence> list=new Vector<FileWithSentence>();
		return treeSearch(list,usedFolderRoot);
	}

	private Vector<FileWithSentence> treeSearch(Vector<FileWithSentence> list,DefaultMutableTreeNode parent) {
		try {
			StimuliFolder parentFolder=null;
			try {
				parentFolder = (StimuliFolder)parent.getUserObject();
			} catch (ClassCastException e) {
			}
			
			DefaultMutableTreeNode node;
			for(int i=0;i<parent.getChildCount();i++)
			{
				node=(DefaultMutableTreeNode) parent.getChildAt(i);
				Object nodeObejct=node.getUserObject();

				if(nodeObejct instanceof StimuliFolder)
				{
//					System.out.println("Folder\t"+nodeObejct);
					treeSearch(list, node);
				}
				else
				{
//					System.out.println("File\t"+nodeObejct);
					try {
						File f=new File(parentFolder.getPath()+File.separator+nodeObejct);
						if(filter.accept(f))
						{
							list.add(new FileWithSentence(f,parentFolder));
						}
					} catch (Exception e) {
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

}
