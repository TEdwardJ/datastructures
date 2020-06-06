package edu.ted.datastructures.map;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class HashMapTestA {

    @Test
    public void givenEmptyMapWhenGetByNullKeyThenNullShouldBeReturned() {
        Map<String, String> map = new HashMap<>();
        assertThat(map.get(null), is(nullValue()));
    }

    @Test
    public void givenEmptyMapWhenGetByNotNullKeyThenNullShouldBeReturned() {
        Map<String, String> map = new HashMap<>();
        assertThat(map.get("key"), is(nullValue()));
    }

    @Test
    public void givenNullKeyWhenPutOnceThenSizeShouldBeEqualToOneAndValueShouldBeEqualToInserted() {
        Map<String, String> map = new HashMap<>();

        String key = null;
        String value = "test";

        map.put(key, value);

        assertThat(map.size(), is(1));
        assertThat(map.get(key), is(value));
    }

    @Test
    public void givenNullKeyWhenPutMultipleTimesThenSizeShouldBeEqualToOneAndValueShouldBeOverwrittenWithLast() {
        Map<String, String> map = new HashMap<>();

        String key = null;
        String firstValue = "test1";
        String secondValue = "test2";
        String thirdValue = "test3";

        map.put(key, firstValue);
        map.put(key, secondValue);
        map.put(key, thirdValue);

        assertThat(map.size(), is(1));
        assertThat(map.get(key), is(thirdValue));
    }

    @Test
    public void givenNotNullKeyWhenPutThenSizeShouldBeEqualToOneAndValueShouldBeEqualToInserted() {
        Map<String, String> map = new HashMap<>();

        String key = "key";
        String value = "value";

        map.put(key, value);

        assertThat(map.size(), is(1));
        assertThat(map.get(key), is(value));
    }

    @Test
    public void givenMultipleNotNullKeysWhenPutThenSizeShouldBeEqualToSizeOfKeysAndGetByKeyReturnsCorrespondingValue() {
        Map<String, String> map = new HashMap<>();

        String firstKey = "key1";
        String secondKey = "key2";
        String firstValue = "value1";
        String secondValue = "value2";

        map.put(firstKey, firstValue);
        map.put(secondKey, secondValue);

        assertThat(map.size(), is(2));
        assertThat(map.get(firstKey), is(firstValue));
        assertThat(map.get(secondKey), is(secondValue));
    }

    @Test
    public void givenMultipleNodesInSameBucketWhenGetByExistingKeyThenGetByKeyReturnsCorrespondingValue() {
        Map<String, String> map = new HashMap<>();

        String firstKey = "key2"; //same bucket
        String secondKey = "key-1"; //same bucket
        String thirdKey = "key-10"; //same bucket

        String firstValue = "value2";
        String secondValue = "value3";
        String thirdValue = "value4";

        map.put(firstKey, firstValue);
        map.put(secondKey, secondValue);
        map.put(thirdKey, thirdValue);

        assertThat(map.size(), is(3));

        assertThat(map.get(firstKey), is(firstValue));
        assertThat(map.get(secondKey), is(secondValue));
        assertThat(map.get(thirdKey), is(thirdValue));
    }

    @Test
    public void givenNotExistingKeyWhenGetByKeyThenNullShouldBeReturned() {
        Map<String, String> map = new HashMap<>();
        map.put("existing key", "value");

        assertThat(map.get("not existing key"), is(nullValue()));
    }

    @Test
    public void givenNotNullKeyWhenPutMultipleTimesWithSameKeyThenSizeShouldBeEqualToOneAndValueShouldBeOverwrittenWithLast() {
        Map<String, String> map = new HashMap<>();

        String key = "key";
        String firstValue = "test1";
        String secondValue = "test2";
        String thirdValue = "test3";

        map.put(key, firstValue);
        map.put(key, secondValue);
        map.put(key, thirdValue);

        assertThat(map.size(), is(1));
        assertThat(map.get(key), is(thirdValue));
    }

    @Test
    public void givenEmptyMapWhenRemoveByNullKeyThenSizeShouldBeEqualToZero() {
        Map<String, String> map = new HashMap<>();
        map.remove(null);
        assertThat(map.size(), is(0));
    }

    @Test
    public void givenEmptyMapWhenRemoveByNotNullKeyThenSizeShouldBeEqualToZero() {
        Map<String, String> map = new HashMap<>();
        map.remove("key");
        assertThat(map.size(), is(0));
    }

    @Test
    public void givenNotEmptyMapWhenRemoveByNullKeyThenSizeShouldBeEqualToZero() {
        Map<String, String> map = new HashMap<>();
        map.put(null, "value");
        map.remove(null);

        assertThat(map.size(), is(0));
    }

    @Test
    public void givenNotEmptyMapWithNotNullKeyWhenPutWithNullKeyAndRemoveByNullKeyThenSizeShouldDecreaseByOne() {
        Map<String, String> map = new HashMap<>();
        map.put(null, "value");
        map.put("not null key", "value");

        assertThat(map.size(), is(2));

        map.remove(null);
        assertThat(map.size(), is(1));
    }

    @Test
    public void givenEmptyMapWhenRemoveThenSizeShouldBeEqualToZero() {
        Map<String, String> map = new HashMap<>();
        map.remove("key");
        assertThat(map.size(), is(0));
    }

    @Test
    public void givenNotEmptyMapWhenRemoveThenSizeShouldBeEqualToZero() {
        Map<String, String> map = new HashMap<>();

        map.put("key", "value");
        assertThat(map.size(), is(1));

        map.remove("key");
        assertThat(map.size(), is(0));
    }

    @Test
    public void givenNotEmptyMapWhenRemoveOneByOneThenSizeShouldDecreaseAfterEachRemovalByOne() {
        Map<String, String> map = new HashMap<>();

        String firstKey = "key1";
        String secondKey = "key2"; //same bucket
        String thirdKey = "key-1"; //same bucket

        String firstValue = "value1";
        String secondValue = "value2";
        String thirdValue = "value3";

        map.put(firstKey, firstValue);
        map.put(secondKey, secondValue); //case: remove first node
        map.put(thirdKey, thirdValue); //case: remove last node

        assertThat(map.size(), is(3));

        map.remove(firstKey);
        assertThat(map.size(), is(2));

        map.remove(secondKey);
        assertThat(map.size(), is(1));

        map.remove(thirdKey);
        assertThat(map.size(), is(0));
    }

    @Test
    public void givenNotEmptyMapAndObjectsInSameBucketWhenRemoveFirstNodeThenSizeShouldDecreaseByOne() {
        Map<String, String> map = new HashMap<>();

        String firstKey = "key1";
        String secondKey = "key2"; //same bucket
        String thirdKey = "key-1"; //same bucket
        String fourthKey = "key-10"; //same bucket

        String firstValue = "value1";
        String secondValue = "value2";
        String thirdValue = "value3";
        String fourthValue = "value4";

        map.put(firstKey, firstValue);
        map.put(secondKey, secondValue);
        map.put(thirdKey, thirdValue);
        map.put(fourthKey, fourthValue);

        assertThat(map.size(), is(4));

        map.remove(secondKey); //case: remove first node
        assertThat(map.size(), is(3));
    }

    @Test
    public void givenNotEmptyMapAndObjectsInSameBucketWhenRemoveLastNodeThenSizeShouldDecreaseByOne() {
        Map<String, String> map = new HashMap<>();

        String firstKey = "key1";
        String secondKey = "key2"; //same bucket
        String thirdKey = "key-1"; //same bucket
        String fourthKey = "key-10"; //same bucket

        String firstValue = "value1";
        String secondValue = "value2";
        String thirdValue = "value3";
        String fourthValue = "value4";

        map.put(firstKey, firstValue);
        map.put(secondKey, secondValue);
        map.put(thirdKey, thirdValue);
        map.put(fourthKey, fourthValue);

        assertThat(map.size(), is(4));

        map.remove(fourthKey); //case: remove first node
        assertThat(map.size(), is(3));
    }

    @Test
    public void givenNotEmptyMapAndObjectsInSameBucketWhenRemoveNodeInTheMiddleThenSizeShouldDecreaseByOne() {
        Map<String, String> map = new HashMap<>();

        String firstKey = "key1";
        String secondKey = "key2"; //same bucket
        String thirdKey = "key-1"; //same bucket
        String fourthKey = "key-10"; //same bucket

        String firstValue = "value1";
        String secondValue = "value2";
        String thirdValue = "value3";
        String fourthValue = "value4";

        map.put(firstKey, firstValue);
        map.put(secondKey, secondValue);
        map.put(thirdKey, thirdValue);
        map.put(fourthKey, fourthValue);

        assertThat(map.size(), is(4));

        map.remove(thirdKey); //case: remove node in the middle
        assertThat(map.size(), is(3));
    }

    @Test
    public void givenEmptyMapNotAndNotExistingKeyWhenRemoveThenSizeShouldNotChange() {
        Map<String, String> map = new HashMap<>();
        map.remove("not existing key");
        assertThat(map.size(), is(0));
    }

    @Test
    public void givenNotEmptyMapNotAndNotExistingKeyWhenRemoveThenSizeShouldNotChange() {
        Map<String, String> map = new HashMap<>();
        map.put("key", "value");

        assertThat(map.size(), is(1));

        map.remove("not existing key");
        assertThat(map.size(), is(1));
    }

    @Test
    public void givenEmptyMapWhenContainsNullKeyThenFalseShouldBeReturned() {
        Map<String, String> map = new HashMap<>();
        assertThat(map.containsKey(null), is(false));
    }

    @Test
    public void givenEmptyMapWhenContainsNotNullKeyThenFalseShouldBeReturned() {
        Map<String, String> map = new HashMap<>();
        assertThat(map.containsKey("key"), is(false));
    }

    @Test
    public void givenMapWithExistingNullKeyWhenContainsNullKeyThenTrueShouldBeReturned() {
        Map<String, String> map = new HashMap<>();
        map.put(null, "value");

        assertThat(map.containsKey(null), is(true));
    }

    @Test
    public void givenMapWithNotExistingNullKeyWhenContainsNullKeyThenFalseShouldBeReturned() {
        Map<String, String> map = new HashMap<>();
        map.put("key", "value");

        assertThat(map.containsKey(null), is(false));
    }

    @Test
    public void givenNotExistingKeyWhenContainsKeyThenFalseShouldBeReturned() {
        Map<String, String> map = new HashMap<>();
        map.put("key", "value");

        assertThat(map.containsKey("key"), is(true));
    }

    @Test
    public void givenExistingKeyWhenContainsKeyThenTrueShouldBeReturned() {
        Map<String, String> map = new HashMap<>();
        map.put("key", "value");

        assertThat(map.containsKey("not existing key"), is(false));
    }

    @Test
    public void givenMultipleNodesInSameBucketAndExistingKeyWhenContainsByKeyThenTrueShouldBeReturned() {
        Map<String, String> map = new HashMap<>();

        String firstKey = "key1";
        String secondKey = "key2"; //same bucket
        String thirdKey = "key-1"; //same bucket
        String fourthKey = "key-10"; //same bucket

        String firstValue = "value1";
        String secondValue = "value2";
        String thirdValue = "value3";
        String fourthValue = "value4";

        map.put(firstKey, firstValue);
        map.put(secondKey, secondValue);
        map.put(thirdKey, thirdValue);
        map.put(fourthKey, fourthValue);

        assertThat(map.containsKey(firstKey), is(true));
        assertThat(map.containsKey(secondKey), is(true));
        assertThat(map.containsKey(thirdKey), is(true));
        assertThat(map.containsKey(fourthKey), is(true));
    }

    @Test
    public void givenMultipleNodesInSameBucketAndNotExistingKeyWithHashLeadingToSameBucketWhenContainsByKeyThenFalseShouldBeReturned() {
        Map<String, String> map = new HashMap<>();

        String firstKey = "key1";
        String secondKey = "key2"; //same bucket
        String thirdKey = "key-1"; //same bucket
        String fourthKey = "key-10"; //same bucket
        String notExistingKeyWithHashLeadingToSameBucket = "key+12";

        String firstValue = "value1";
        String secondValue = "value2";
        String thirdValue = "value3";
        String fourthValue = "value4";

        map.put(firstKey, firstValue);
        map.put(secondKey, secondValue);
        map.put(thirdKey, thirdValue);
        map.put(fourthKey, fourthValue);

        assertThat(map.containsKey(firstKey), is(true));
        assertThat(map.containsKey(secondKey), is(true));
        assertThat(map.containsKey(thirdKey), is(true));
        assertThat(map.containsKey(fourthKey), is(true));
        assertThat(map.containsKey(notExistingKeyWithHashLeadingToSameBucket), is(false));
    }



    @Test//(expected = NoSuchElementException.class)
    public void catchGivenEmptyMapWhenIteratorNextThenNoSuchElementExceptionShouldBeRaised(){
        assertThrows(NoSuchElementException.class, ()->givenEmptyMapWhenIteratorNextThenNoSuchElementExceptionShouldBeRaised());
    }
    public void givenEmptyMapWhenIteratorNextThenNoSuchElementExceptionShouldBeRaised() {
        new HashMap<>().iterator().next();
    }

    @Test
    public void catchGivenIteratorWhenNextAfterLastElementThenNoSuchElementExceptionShouldBeRaised(){
        assertThrows(NoSuchElementException.class, ()->givenIteratorWhenNextAfterLastElementThenNoSuchElementExceptionShouldBeRaised());
    }

    public void givenIteratorWhenNextAfterLastElementThenNoSuchElementExceptionShouldBeRaised() {
        HashMap<String, String> map = new HashMap<>();

        String key = "key";
        String value = "value";
        map.put(key, value);

        Iterator<Map.Entry<String, String>> iterator = map.iterator();

        Map.Entry<String, String> entry = iterator.next();
        assertThat(entry.getKey(), is(key));
        assertThat(entry.getValue(), is(value));

        iterator.next();
    }

    @Test
    public void givenIteratorWhenNextThenShouldReturnNextValue() {
        HashMap<String, String> map = new HashMap<>();

        String keyQ = "0";
        String keyW = "key1";
        String keyE = "key2";
        String keyR = "key-1";
        String keyT = "key-10";
        String keyY = "key-100";
        String keyZ = "1000";

        String keyQValue = "keyQValue";
        String keyWValue = "keyWValue";
        String keyEValue = "keyEValue";
        String keyRValue = "keyRValue";
        String keyTValue = "keyTValue";
        String keyYValue = "keyYValue";
        String keyZValue = "keyZValue";

        map.put(keyQ, keyQValue);
        map.put(keyW, keyWValue);
        map.put(keyE, keyEValue);
        map.put(keyR, keyRValue);
        map.put(keyT, keyTValue);
        map.put(keyY, keyYValue);
        map.put(keyZ, keyZValue);

        Iterator<Map.Entry<String, String>> iterator = map.iterator();

        Map.Entry<String, String> resultQ = iterator.next();
        Map.Entry<String, String> resultW = iterator.next();
        Map.Entry<String, String> resultE = iterator.next();
        Map.Entry<String, String> resultR = iterator.next();
        Map.Entry<String, String> resultT = iterator.next();
        Map.Entry<String, String> resultY = iterator.next();
        Map.Entry<String, String> resultZ = iterator.next();

        assertThat(resultQ.getKey(), is(keyQ));
        assertThat(resultQ.getValue(), is(keyQValue));

        assertThat(resultW.getKey(), is(keyW));
        assertThat(resultW.getValue(), is(keyWValue));

        assertThat(resultE.getKey(), is(keyE));
        assertThat(resultE.getValue(), is(keyEValue));

        assertThat(resultR.getKey(), is(keyR));
        assertThat(resultR.getValue(), is(keyRValue));

        assertThat(resultT.getKey(), is(keyT));
        assertThat(resultT.getValue(), is(keyTValue));

        assertThat(resultY.getKey(), is(keyY));
        assertThat(resultY.getValue(), is(keyYValue));

        assertThat(resultZ.getKey(), is(keyZ));
        assertThat(resultZ.getValue(), is(keyZValue));
    }

    @Test
    public void givenEmptyMapWhenIteratorHasNextThenShouldReturnFalse() {
        HashMap<String, String> map = new HashMap<>();
        assertThat(map.iterator().hasNext(), is(false));
    }

    @Test
    public void givenNotEmptyMapWhenIteratorHasNextThenShouldReturnTrue() {
        HashMap<String, String> map = new HashMap<>();
        map.put("key", "value");

        Iterator<Map.Entry<String, String>> iterator = map.iterator();
        assertThat(iterator.hasNext(), is(true));
        assertThat(iterator.hasNext(), is(true));

        map.remove("key");
        assertThat(iterator.hasNext(), is(false));

        map.put("key", "value");
        assertThat(iterator.hasNext(), is(true));
    }

    @Test
    public void givenMapWithTwoElementsWhenIteratorNextThenIteratorHasNextShouldReturnFalse() {
        HashMap<String, String> map = new HashMap<>();
        map.put("key", "value");
        map.put("key2", "value");

        Iterator<HashMap.Entry<String, String>> iterator = map.iterator();

        assertThat(iterator.hasNext(), is(true));
        iterator.next();
        assertThat(iterator.hasNext(), is(true));
    }

    @Test
    public void givenMapWithOneElementWhenIteratorNextThenIteratorHasNextShouldReturnFalse() {
        HashMap<String, String> map = new HashMap<>();
        map.put("key", "value");

        Iterator<HashMap.Entry<String, String>> iterator = map.iterator();

        assertThat(iterator.hasNext(), is(true));
        iterator.next();
        assertThat(iterator.hasNext(), is(false));
    }

    @Test//(expected = IllegalStateException.class)
    public void catchGivenEmptyMapWhenIteratorRemoveThenNoSuchElementExceptionShouldBeRaised(){
        assertThrows(IllegalStateException.class, ()->givenEmptyMapWhenIteratorRemoveThenNoSuchElementExceptionShouldBeRaised());
    }

    public void givenEmptyMapWhenIteratorRemoveThenNoSuchElementExceptionShouldBeRaised() {
        new HashMap<>().iterator().remove();
    }

    @Test//(expected = IllegalStateException.class)
    public void catchGivenIteratorWhenRemoveCalledWithoutNextThenIllegalStateExceptionShouldBeRaised(){
        assertThrows(IllegalStateException.class, ()->givenIteratorWhenRemoveCalledWithoutNextThenIllegalStateExceptionShouldBeRaised());
    }

    @Test//(expected = IllegalStateException.class)
    public void cAtchGivenIteratorWhenRemoveCalledWithoutNextThenIllegalStateExceptionShouldBeRaised(){
        assertThrows(IllegalStateException.class, ()->givenIteratorWhenRemoveCalledWithoutNextThenIllegalStateExceptionShouldBeRaised());
    }

    public void givenIteratorWhenRemoveCalledWithoutNextThenIllegalStateExceptionShouldBeRaised() {
        HashMap<String, String> map = new HashMap<>();
        map.put("key", "value");

        assertThat(map.size(), is(1));

        map.iterator().remove();
    }

    @Test
    public void givenIteratorWhenRemoveCalledAfterNextThenSizeShouldBeDecreasedByOneAndMapShouldNotContainKey() {
        HashMap<String, String> map = new HashMap<>();
        String key = "key";
        map.put(key, "value");
        assertThat(map.size(), is(1));

        Iterator<HashMap.Entry<String, String>> iterator = map.iterator();
        iterator.next();
        iterator.remove();

        assertThat(map.size(), is(0));
        assertThat(map.containsKey(key), is(false));
    }
}
