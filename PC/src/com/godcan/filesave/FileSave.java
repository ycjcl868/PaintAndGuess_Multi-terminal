package com.godcan.filesave;

import java.io.*;

import javax.swing.*;

import com.godcan.entity.Drawing;
import com.godcan.paint.DrawArea;

//文件类 （文件的打开、新建、保存）
public class FileSave {
	
    private DrawArea drawArea;
    
    public FileSave(DrawArea drawPanel) {
    	this.drawArea = drawPanel;
	}
    
    /**
     * 保存图像
     */
	public void saveFile() {
		
		//JFileChooser 为用户选择文件提供了一种简单的机制
		JFileChooser filechooser = new JFileChooser();
		filechooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		//setFileSelectionMode()设置 JFileChooser，以允许用户只选择文件、只选择目录，或者可选择文件和目录。
		int result = filechooser.showSaveDialog(drawArea);
		if(result == JFileChooser.CANCEL_OPTION){
        	return ;
        }
        
        File fileName = filechooser.getSelectedFile();//getSelectedFile()返回选中的文件
	    fileName.canWrite();//测试应用程序是否可以修改此抽象路径名表示的文件
	    if(fileName == null || fileName.getName().equals(""))//文件名不存在时
	    {
	    	JOptionPane.showMessageDialog(filechooser,"文件名","请输入文件名！",JOptionPane.ERROR_MESSAGE);
	    } else {
	    	try {
				fileName.delete();//删除此抽象路径名表示的文件或目录
				FileOutputStream fos = new FileOutputStream(fileName+".xxh");//文件输出流以字节的方式输出
				//对象输出流
				ObjectOutputStream output = new ObjectOutputStream(fos);
				//Drawing record;
				
				output.writeInt(drawArea.getIndex());
				
				for(int i = 0;i < drawArea.getIndex(); i++) {
					Drawing p = drawArea.getItemList().get(i);
					output.writeObject(p);
					output.flush();//刷新该流的缓冲。此操作将写入所有已缓冲的输出字节，并将它们刷新到底层流中。
					               //将所有的图形信息强制的转换成父类线性化存储到文件中    
				}
				output.close();
				fos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
	    }
	}
}
