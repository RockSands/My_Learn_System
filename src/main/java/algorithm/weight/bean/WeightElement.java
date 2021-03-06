package algorithm.weight.bean;

/**
 * 权重元素
 * 
 * @author Administrator
 *
 */
public class WeightElement {
	/**
	 * key
	 */
	private final String key;

	/**
	 * 计数器
	 */
	private long counter;

	/**
	 * 权重,目标权重
	 */
	private long weight;
	
	/**
	 * 运行时权重,负责计算
	 */
	private long runtimeWeight;

	/**
	 * 当前权重
	 */
	private long currentWeight;

	/**
	 * @param key
	 * @param weight
	 */
	public WeightElement(String key, long weight) {
		this(key, 0, weight);
	}

	/**
	 * @param key
	 * @param hitTimes
	 * @param weight
	 */
	public WeightElement(String key, long counter, long weight) {
		this.key = key;
		this.counter = counter;
		this.weight = weight;
	}
	
	/**
	 * 结算
	 */
	public void settlement(){
		counter = 0l;
		currentWeight = 0l;
		runtimeWeight = weight;
	}
	
	/**
	 * 添加权重
	 * @param weightSum
	 */
	public void addWeight(){
		currentWeight = currentWeight + runtimeWeight;
	}
	
	/**
	 * 命中
	 */
	public void hit(long weightSum){
		counter = counter + 1;
		currentWeight = currentWeight - weightSum;
	}

	/**
	 * @return the counter
	 */
	public long getCounter() {
		return counter;
	}

	/**
	 * @param counter the counter to set
	 */
	public void setCounter(long counter) {
		this.counter = counter;
	}

	/**
	 * @return the weight
	 */
	public long getWeight() {
		return weight;
	}

	/**
	 * @param weight the weight to set
	 */
	public void setWeight(long weight) {
		this.weight = weight;
	}

	/**
	 * @return the runtimeWeight
	 */
	public long getRuntimeWeight() {
		return runtimeWeight;
	}

	/**
	 * @param runtimeWeight the runtimeWeight to set
	 */
	public void setRuntimeWeight(long runtimeWeight) {
		this.runtimeWeight = runtimeWeight;
	}

	/**
	 * @return the currentWeight
	 */
	public long getCurrentWeight() {
		return currentWeight;
	}

	/**
	 * @param currentWeight the currentWeight to set
	 */
	public void setCurrentWeight(long currentWeight) {
		this.currentWeight = currentWeight;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}
}
