/**
 * <p>Title: Collections </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2012</p>
 * <p>Company: creditcities</p>
 * @author mshi
 * @version 1.0
 * @function list
 * 1.ArrayList
 * 2.HashMap
 */

function ArrayList() {
    this.count = 0;
    this.elementData = new Array();
}

ArrayList.prototype.size = size_ArrayList;
ArrayList.prototype.isEmpty = isEmpty_ArrayList;
ArrayList.prototype.indexOf = indexOf_ArrayList;
ArrayList.prototype.contains = contains_ArrayList;
ArrayList.prototype.get = get_ArrayList;
ArrayList.prototype.add = add_ArrayList;
ArrayList.prototype.insert = insert_ArrayList;
ArrayList.prototype.remove = remove_ArrayList;
ArrayList.prototype.clear = clear_ArrayList;
ArrayList.prototype.toString = toString_ArrayList;
ArrayList.prototype.checkRange = checkRange_ArrayList;

function size_ArrayList() {
    return this.count;
}

function isEmpty_ArrayList() {
    return this.count == 0;
}

function indexOf_ArrayList(elem) {
    if (!elem) {
        for (var i = 0; i < this.count; i++) {
            if(!this.elementData[i])
                return i;
        }
    } else {
        if(elem.equals) {
            for (var i = 0; i < this.count; i++) {
                if(elem.equals(this.elementData[i])) 
                    return i;
            }
        } else {
            for (var i = 0; i < this.count; i++) {
                if(elem == this.elementData[i]) 
                    return i;
            }
        }
    }
    return -1;
}

function contains_ArrayList(elem) {
    return this.indexOf(elem) >= 0;
}

function get_ArrayList(index) {
    this.checkRange(index);
    return this.elementData[index];
}

function add_ArrayList(elem) {
    this.elementData[this.count++] = elem;
    return true;
}

function insert_ArrayList(index, elem) {
    if (index == this.count) this.add(elem);
    this.checkRange(index);
    var originCount = this.count;
    for (var i = originCount - 1; i >= index; i--) {
    	this.elementData[i + 1] = this.elementData[i];
    }
    this.elementData[index] = elem;
    this.count++;
    return true;
}

function remove_ArrayList(index) {
    this.checkRange(index);
    var oldValue = this.get(index);
    this.elementData = this.elementData.slice(0, index).concat(this.elementData.slice(index + 1, this.count));
    this.count--;
    return oldValue;
}

function clear_ArrayList() {
    this.elementData.length = 0;
    this.count = 0;
}

function toString_ArrayList() {
    var buf = "[";
    var maxIndex = this.size() - 1;
    for (var i = 0; i <= maxIndex; i++) {
        buf += this.elementData[i].toString();
        if(i < maxIndex) buf += ",";
    }
    buf += "]";
    return buf;
}

function checkRange_ArrayList(index) {
    if (index >= this.count || index < 0) {
        throw new Error("Invalid parameter: Index: "+index+", Size: "+this.count);
    }
}

function HashMap() {
    this.count = 0;
    this.tableData = new Array();
}

HashMap.prototype.size = size_HashMap;
HashMap.prototype.isEmpty = isEmpty_HashMap;
HashMap.prototype.containsKey = containsKey_HashMap;
HashMap.prototype.get = get_HashMap;
HashMap.prototype.put = put_HashMap;
HashMap.prototype.remove = remove_HashMap;
HashMap.prototype.clear = clear_HashMap;
HashMap.prototype.keySet = keySet_HashMap;
HashMap.prototype.values = values_HashMap;
HashMap.prototype.toString = toString_HashMap;

function size_HashMap() {
    return this.count;
}

function isEmpty_HashMap() {
    return this.count == 0;
}

function containsKey_HashMap(key) {
    if (!key) {
        throw new Error("Invalid parameter: parameter can't be null.");
    } else {
        for (var i = 0; i < this.count; i++) {
            if (key == this.tableData[i].key) {
                return true;
            }
        }
    }
    return false;
}

function get_HashMap(key) {
    if (!key) {
        throw new Error("Invalid parameter: parameter can't be null.");
    } else {
        for (var i = 0; i < this.count; i++) {
            if (key == this.tableData[i].key) {
                return this.tableData[i].value;
            }
        }
    }
    return null;
}

function put_HashMap(key, value) {
    if (key == null) {
        throw new Error("Invalid parameter: first parameter can't be null.");
    }
    if (value == null) {
        throw new Error("Invalid parameter: second parameter can't be null.");
    }
    var oldValue = this.remove(key);
    var entry = {};
    entry.key = key;
    entry.value = value;
    this.tableData[this.count++] = entry;
    return oldValue;
}

function remove_HashMap(key) {
    var index = -1;
    var oldValue = null;
    if (!key) {
        throw new Error("Invalid parameter: parameter can't be null.");
    } else {
        for (var i = 0; i < this.count; i++) {
            if (key == this.tableData[i].key) {
                index = i;
                oldValue = this.tableData[i].value;
                break;
            }
        }
    }
    if (index == -1) {
        return null;
    }
    this.tableData = this.tableData.slice(0, index).concat(this.tableData.slice(index + 1, this.count));
    this.count--;
    return oldValue;
}

function clear_HashMap() {
    this.tableData.length = 0;
    this.count = 0;
}

function keySet_HashMap() {
    var keySet = [];
    for (var i = 0; i < this.tableData.length; i++)  {
        keySet[i] = this.tableData[i].key;
    }
    return keySet;
}

function values_HashMap() {
    var values = [];
    for (var i = 0; i < this.tableData.length; i++)  {
        values[i] = this.tableData[i].value;
    }
    return values;
}

function toString_HashMap() {
    var max = this.size() - 1;
    var buf = "{";
    for (var i = 0; i <= max; i++) {
        var key = this.tableData[i].key;
        buf += (key + "=" + this.get(key).toString());
        if (i < max) buf += ", ";
    }
    buf += "}";
    return buf;
}