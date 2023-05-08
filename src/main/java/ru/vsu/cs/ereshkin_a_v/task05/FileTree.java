package ru.vsu.cs.ereshkin_a_v.task05;

import java.awt.*;
import java.io.File;
import java.io.FileFilter;
import java.util.*;
import java.util.List;
import java.util.function.Function;

public class FileTree {

	protected FileTreeNode root = null;
	protected Function<File, String> toStrFunc;

	public FileTree(Function<File, String> toStrFunc) {
		this.toStrFunc = toStrFunc;
	}
	@SuppressWarnings("unused")
	public FileTree() {
		this(Object::toString);
	}
	@SuppressWarnings("unused")
	public FileTree(File rootDirectory) {
		this(Object::toString);
		setRoot(rootDirectory);
	}

	public FileTree(String rootDirectoryPath) {
		this(Object::toString);
		setRoot(new File(rootDirectoryPath));
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

	public String toBracketNotation() throws Exception {
		if (toStrFunc == null) {
			throw new Exception("Не определена функция конвертации в строку");
		}
		return toBracketNotation(root);
	}

	private String toBracketNotation(FileTreeNode parentNode) {
		StringBuilder str = new StringBuilder();
		str.append('[');
		str.append('\"');
		str.append(toStrFunc.apply(parentNode.value));
		str.append('\"');
		str.append(']');
		boolean existChildrenValues = parentNode.numberOfChildrenValues() != 0;
		boolean existChildrenNodes = parentNode.numberOfChildrenNodes() != 0;
		if (!existChildrenValues && !existChildrenNodes) return str.toString();
		str.append('(');
		/// Поддиректории
		for (int i = 0; i < parentNode.numberOfChildrenNodes() - 1; i++) {
			str.append(toBracketNotation(parentNode.getChildNode(i)));
			str.append(",");
		}
		str.append('\"');
		str.append(toBracketNotation(parentNode.getChildNode(parentNode.numberOfChildrenNodes() - 1)));
		str.append('\"');
		/// Файлы
		List<File> childValues = parentNode.childValues;
		if (!childValues.isEmpty() && !parentNode.childNodes.isEmpty()) str.append(", ");
		for (int i = 0; i < parentNode.numberOfChildrenValues() - 1; i++) {
			str.append('\"');
			str.append(childValues.get(i).getName());
			str.append('\"');
			str.append(",");
		}
		str.append('\"');
		str.append(childValues.get(parentNode.numberOfChildrenValues() - 1).getName());
		str.append('\"');
		str.append(')');
		return str.toString();
	}

	public List<File> searchByNameAndExtension(String fileFullName) {
		List<File> resultList = new ArrayList<>();

		return resultList;
	}

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
		 * */
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