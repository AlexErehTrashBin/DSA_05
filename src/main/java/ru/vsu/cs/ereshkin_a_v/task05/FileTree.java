package ru.vsu.cs.ereshkin_a_v.task05;

import java.io.File;
import java.io.FileFilter;
import java.util.*;
import java.util.function.Function;

@SuppressWarnings("unused")
public class FileTree implements Iterable<File> {

	protected FileTreeNode root = null;
	protected Function<File, String> toStrFunc;

	public FileTree(Function<File, String> toStrFunc) {
		this.toStrFunc = toStrFunc;
	}

	public FileTree() {
		this(Object::toString);
	}

	public FileTree(File rootDirectory) {
		this(Object::toString);
		setRoot(rootDirectory);
	}

	public FileTree(String rootDirectoryPath) {
		this(Object::toString);
		setRoot(new File(rootDirectoryPath));
	}

	private static void expand(File file, FileFilter fileFilter,
	                   Queue<File> outFiles, Queue<File> outDirs) {
		if (file == null) return;
		File[] list = file.listFiles();
		if (list == null) return;
		for (File child : list) {
			if (child == null) continue;
			boolean isDir = child.isDirectory();
			if (isDir) {
				outDirs.offer(child);
			}

			if (fileFilter == null || fileFilter.accept(child)) {
				outFiles.offer(child);
			}
		}
	}

	private static boolean isDirectory(File file) {
		return file != null && file.exists() && file.isDirectory();
	}

	public FileTreeNode getRoot() {
		return root;
	}

	public File getRootFile() {
		return root.getValue();
	}

	public void setRoot(File value) {
		root = new FileTreeNode(value);
		root.traverseRootAndAddNodes();
	}

	public void clear() {
		root = null;
	}

	private void skipSpaces(String bracketStr, IndexWrapper iw) {
		while (iw.index < bracketStr.length() && Character.isWhitespace(bracketStr.charAt(iw.index))) {
			iw.index++;
		}
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
			//str.append('\"');
			str.append(toBracketNotation(parentNode.getChildNode(i)));
			//str.append('\"');
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

	@Override
	public Iterator<File> iterator() {
		return new FileTreeIterator(this.root.getValue(), null);
	}

	public List<File> searchByNameAndExtension(String fileFullName) {
		List<File> resultList = new ArrayList<>();
		for (File file : this) {
			if (file.getName().equals(fileFullName)) System.out.println(file.getName());
		}
		return resultList;
	}

	private static class FileTreeIterator implements Iterator<File> {
		private final Queue<File> dirs = new LinkedList<>();
		private final Queue<File> files = new LinkedList<>();
		private final FileFilter fileFilter;

		private FileTreeIterator(File root, FileFilter fileFilter) {
			if (isDirectory(root)) {
				this.dirs.add(root);
			}
			this.fileFilter = fileFilter;
		}

		public boolean hasNext() {
			if (this.files.peek() == null)
				expandUntilFilesFound();
			return this.files.peek() != null;
		}

		public File next() {
			if (this.files.peek() == null) {
				expandUntilFilesFound();
				if (this.files.peek() == null)
					throw new NoSuchElementException();
			}
			return this.files.poll();
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

		private void expandUntilFilesFound() {
			while (this.dirs.peek() != null && this.files.peek() == null)
				expand(this.dirs.poll());
		}

		private void expand(File directory) {
			if (directory != null) {
				FileTree.expand(directory, this.fileFilter, this.files, this.dirs);
			}
		}
	}

	public class FileTreeNode {
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

		public FileTreeNode(File value, List<FileTreeNode> childNodes) {
			this.value = value;
			this.childNodes = childNodes;
			this.childValues = new ArrayList<>();
		}

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

		public void addChildNode(File value) {
			childNodes.add(new FileTreeNode(value));
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
					//outDirs.offer(child);
				} else {
					addChildValue(child);
				}
				//childValues.add(child);
			}
		}

		@Override
		public String toString() {
			return this.value.getName();
		}
	}

	private class IndexWrapper {
		public int index = 0;
	}

}