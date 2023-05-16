package ru.vsu.cs.ereshkin_a_v.task05;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class FileTree {

	protected FileTreeNode root = null;

	@SuppressWarnings("unused")
	public FileTree(File rootDirectory) {
		setRoot(rootDirectory);
	}

	public FileTree(String rootDirectoryPath) {
		setRoot(new File(rootDirectoryPath));
	}

	public static void applyLambdaWhileIterating(
			FileTreeNode node, Consumer<File> consumer) {
		for (FileTreeNode childNode : node.getChildNodes()) {
			applyLambdaWhileIterating(childNode, consumer);
		}
		for (File value : node.getChildValues()) {
			consumer.accept(value);
		}
	}

	public FileTreeNode getRoot() {
		return root;
	}

	public void setRoot(File value) {
		root = new FileTreeNode(value);
		root.traverseRootAndAddNodes();
	}

	public void clear() {
		root = null;
	}

	// TODO Регулярные выражения / соло расширения
	public List<File> searchByNameAndExtension(String fileFullName) {
		List<File> resultList = new ArrayList<>();
		FileTreeNode root = getRoot();
		applyLambdaWhileIterating(root, (File file) -> {
			if (file.getName().equals(fileFullName)) {
				resultList.add(file);
			}
		});
		return resultList;
	}
	// TODO - узлы дерева - не только директории,
	//  но и файлы => список дочерних узлов один, он равен null если у нас файл
	public static class FileTreeNode {
		/**
		 * Сама директория (поддиректория)
		 */
		private final File value;
		/**
		 * Дочерние узлы, они же поддиректории
		 */
		private final List<FileTreeNode> childNodes;
		/**
		 * Листья, они же обычные файлы
		 */
		private final List<File> childValues;

		public FileTreeNode(File value) {
			this.value = value;
			this.childNodes = new ArrayList<>();
			this.childValues = new ArrayList<>();
		}

		public File getValue() {
			return value;
		}

		public List<FileTreeNode> getChildNodes() {
			return new ArrayList<>(childNodes);
		}

		public List<File> getChildValues() {
			return new ArrayList<>(childValues);
		}

		public void addChildNode(FileTreeNode node) {
			childNodes.add(node);
		}

		public void addChildValue(File value) {
			childValues.add(value);
		}

		public FileTreeNode getChildNode(int index) {
			return childNodes.get(index);
		}

		public File getChildValue(int index) {
			return childValues.get(index);
		}

		public int numberOfChildrenNodes() {
			return childNodes.size();
		}

		public int numberOfChildrenValues() {
			return childValues.size();
		}

		/**
		 * Метод для рекурсивного заполнения дерева файлов.
		 */
		private void traverseRootAndAddNodes() {
			if (this.getValue() == null) return;
			File[] list = this.getValue().listFiles();
			if (list == null) return;
			for (File child : list) {
				if (child == null) continue;
				boolean isDir = child.isDirectory();
				if (isDir) {
					FileTreeNode node = new FileTreeNode(child);
					addChildNode(node);
					node.traverseRootAndAddNodes();
				} else {
					addChildValue(child);
				}
			}
		}

		@Override
		public String toString() {
			return this.value.getName();
		}
	}

}