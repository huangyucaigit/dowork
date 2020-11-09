package dictionary;

/**
 * 数据操作
 */
public enum DataOperation {
	新增,
	更新,
	删除;
	
    public String getValue(){
        return this.toString();
    }
}
