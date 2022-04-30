package edu.vanderbilt.studzikm;

/**
 * Represents a resource that country's possess
 */
public class Resource {
	
	private String name;
	private Double weight;

	/**
	 * Creates a resource
	 * @param name the name of the resource
	 * @param weight the relative importance of a resource to countries
	 */
	public Resource(String name, Double weight) {
		super();
		this.name = name;
		this.weight = weight;
	}

	/**
	 * Gets the name of the resource
	 * @return the resource name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the resource
	 * @param name the name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the weight of the resource
	 * @return the resource weight
	 */
	public Double getWeight() {
		return weight;
	}

	/**
	 * Sets the weight of the resource
	 * @param weight the weight to set
	 */
	public void setWeight(Double weight) {
		this.weight = weight;
	}

	@Override
	public String toString() {
		return "Resource [name=" + name + ", weight=" + weight + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((weight == null) ? 0 : weight.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Resource other = (Resource) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (weight == null) {
			if (other.weight != null)
				return false;
		} else if (!weight.equals(other.weight))
			return false;
		return true;
	}

}
