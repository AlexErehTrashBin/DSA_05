package ru.vsu.cs.ereshkin_a_v.task05;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;

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
		for (FileTreeNode childNode : node.getChildren()) {
			applyLambdaWhileIterating(childNode, consumer);
		}
		for (File value : node.children.stream()
				.map(it -> it.value)
				.collect(Collectors.toList())) {
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

	public List<File> searchByNameAndExtension(String predicate) {
		Pattern pattern;
		try {
			predicate = predicate.replaceAll("[.]", "[.]");
			predicate = predicate.replaceAll("[*]", ".*");
			pattern = Pattern.compile(predicate);
		} catch (PatternSyntaxException ex){
			return new ArrayList<>();
		}
		List<File> resultList = new ArrayList<>();
		FileTreeNode root = getRoot();
		Pattern finalPattern = pattern;
		final String predicateFinal = predicate;
		applyLambdaWhileIterating(root, (File file) -> {
			String fileName = file.getName();
			if (finalPattern.matcher(fileName).matches()) {
				resultList.add(file);
				return;
			}
			if (fileName.equals(predicateFinal)){
				resultList.add(file);
				return;
			}
			if (fileName.endsWith(predicateFinal)){
				resultList.add(file);
			}

		});
		return resultList;
	}

	public static class FileTreeNode {
		/**
		 * Сама директория (поддиректория)
		 */
		private final File value;

		private final List<FileTreeNode> children;

		public FileTreeNode(File value) {
			this.value = value;
			this.children = new ArrayList<>();
		}

		public File getValue() {
			return value;
		}

		public List<FileTreeNode> getChildren() {
			return new ArrayList<>(children);
		}

		public List<File> getChildrenAsFiles() {
			return new ArrayList<>(children).stream()
					.map((it) -> it.value)
					.collect(Collectors.toList());
		}

		public List<File> getChildFiles() {
			return children.stream()
					.map(it -> it.value)
					.filter((it) -> !it.isDirectory())
					.collect(Collectors.toList());
		}

		public List<File> getChildDirectories() {
			return children.stream()
					.map(it -> it.value)
					.filter(File::isDirectory)
					.collect(Collectors.toList());
		}


		public void addChild(FileTreeNode node) {
			children.add(node);
		}


		public FileTreeNode getChild(int index) {
			return children.get(index);
		}


		public int numberOfChildren() {
			return children.size();
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
					addChild(node);
					node.traverseRootAndAddNodes();
				} else {
					addChild(new FileTreeNode(child));
				}
			}
		}

		@Override
		public String toString() {
			return this.value.getName();
		}

		public boolean isDirectory() {
			return value.isDirectory();
		}
	}

}