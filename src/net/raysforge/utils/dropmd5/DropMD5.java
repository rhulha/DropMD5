package net.raysforge.utils.dropmd5;

import java.io.File;
import java.util.List;

import javax.swing.JScrollPane;

import net.raysforge.easyswing.EasySwing;
import net.raysforge.easyswing.EasyTable;
import net.raysforge.easyswing.FileDropListener;

public class DropMD5 implements FileDropListener {

	private JScrollPane scrollPane;
	private EasyTable easyTable;
	private CalcHash ch;

	public DropMD5() {
		EasySwing es = new EasySwing("DropMD5", 800, 600);

		easyTable = es.setTableAsMainContent();

		easyTable.addColumn("Path");
		easyTable.addColumn("MD5");

		scrollPane = easyTable.getScrollPane();

		es.addEasyFileDropTarget(scrollPane, this);

		es.show();

		ch = new CalcHash(easyTable);
		ch.start();
	}

	public static void main(String[] args) {
		new DropMD5();
	}

	@Override
	public void filesDropped(List<File> list) {
		for (File file : list) {
			int row = easyTable.addValue(file.getAbsolutePath());
			ch.add(row - 1);
		}
	}

}
