import java.lang.annotation.Target;
import java.util.Scanner;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

// HashTable class -- implements a hash table with linear probing
public class HashTable {

	public String[] table;				// Table of inserted elements
	public int occupied;
	public int mostRecent;
	public String hashType;

	// Constructor -- allocate the table
	public HashTable(int capacity, String hashType) {
		this.table = new String[capacity];
		this.occupied = 0;
		this.mostRecent = 0;
		this.hashType = hashType;
	}

	public int size() {
		return occupied;
	}

	public int numCollisions() {
		return mostRecent;
	}

	public int capacity() {
		return table.length;
	}

	// Compute the ratio of inserted elements to capacity
	public double loadFactor() {
		return (double) occupied/table.length;
	}

	// Compute the primary hash of the given string
	private int hash1(String str) {
		return switch (hashType) {
			case "crc32-lp", "crc32-dh" -> Hasher.crc32(str);
			case "adler32-lp", "adler32-dh" -> (Hasher.adler32(str));
			case "murmur3-lp", "murmur3-dh" -> (Hasher.murmur3_32(str, 0));
			case "poly-lp", "poly-dh" -> (Hasher.polynomial(str, 0));
			default -> 0;
		};
	}

	// Compute the secondary hash of the given string
	private int hash2(String str) {
		return switch (hashType) {
			case "crc32-dh" -> (Hasher.adler32(str));
			case "adler32-dh" -> (Hasher.crc32(str));
			case "murmur3-dh" -> (Hasher.murmur3_32(str, 1));
			case "poly-dh" -> (Hasher.polynomial(str, 1));
			default -> 0;
		};
	}

	// Insert a string into the hash table and return whether successful
	public boolean insert(String str) {
		switch (hashType) {
			case "crc32-lp" :
			case "adler32-lp" :
			case "murmur3-lp" :
			case "poly-lp":
				int index = Integer.remainderUnsigned(hash1(str), table.length);
				int temp = 0;
				if (occupied == table.length) {
					return false;
				}
				if (table[index] != null && table[index].equals(str)) {
						return false;
				}
				while (table[index] != null) {
					if (table[index].equals(str)) {
						return false;
					}
					temp++;
					index = (index+1)%table.length;
					if (temp >= table.length) {
						return false;
					}
				}
				table[index] = str;
				mostRecent = temp;
				occupied++;
				return true;
			case "crc32-dh" :
			case "murmur3-dh" :
			case "adler32-dh" :
			case "poly-dh" :
				index = Integer.remainderUnsigned(hash1(str), table.length);
				temp = 0;
				int j = 0;
				if (occupied == table.length) {
					return false;
				}
				if (table[index] != null && table[index].equals(str)) {
					return false;
				}
				while (table[index] != null) {
					if (table[index].equals(str)) {
						return false;
					}
					index = Integer.remainderUnsigned((hash1(str) + j * hash2(str)), table.length);
					j++;
					temp++;
					if (temp >= table.length) {
						return false;
					}
				}
				table[Integer.remainderUnsigned(index, table.length)%table.length] = str;
				mostRecent = temp;
				occupied++;
				return true;
			default:
				return false;
		}
	}

	// Return whether the table contains the string
	public boolean contains(String str) {
		return false;
	}


	public static void main(String[] args) {
		// TODO: Implement experiments from section 1.3 in the handout here

	}
}
