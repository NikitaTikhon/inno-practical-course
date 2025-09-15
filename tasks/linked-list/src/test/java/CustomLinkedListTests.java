import com.innowise.list.CustomLinkedList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CustomLinkedListTests {

    @Nested
    @DisplayName("Filled list")
    public class FilledList {
        private final CustomLinkedList<String> list = new CustomLinkedList<>();

        @BeforeEach
        void setUp() {
            list.addLast("Hello");
            list.addLast("World");
        }

        @Test
        @DisplayName("Check list size")
        public void checkListSize() {
            assertEquals(2, list.size());

            list.addFirst("New element");

            assertEquals(3, list.size());
        }

        @Test
        @DisplayName("Add first element in the beginning of the list")
        public void addFirstElement() {
            list.addFirst("First");

            assertEquals(3, list.size());
            assertEquals("First", list.getFirst());
        }

        @Test
        @DisplayName("Add last element in the end of the list")
        public void addLastElement() {
            list.addLast("Last");

            assertEquals(3, list.size());
            assertEquals("Last", list.getLast());
        }

        @Test
        @DisplayName("Add the element in the list by index")
        public void addElementByIndex() {
            list.add(2,"Index");

            assertEquals(3, list.size());
            assertEquals("Index", list.get(2));
        }

        @Test
        @DisplayName("Return the first element of the list")
        public void returnFirstElement() {
            assertEquals("Hello", list.getFirst());
        }

        @Test
        @DisplayName("Return the last element of the list")
        public void returnLastElement() {
            assertEquals("World", list.getLast());
        }

        @Test
        @DisplayName("Return the element by index")
        public void returnElementByIndex() {
            assertEquals("Hello", list.get(0));
        }

        @Test
        @DisplayName("Retrieve and remove the first element of the list")
        public void removeFirstElement() {
            String removedElement = list.removeFirst();

            assertEquals(1, list.size());
            assertEquals("Hello", removedElement);
        }

        @Test
        @DisplayName("Retrieve and remove the last element of the list")
        public void removeLastElement() {
            String removedElement = list.removeLast();

            assertEquals(1, list.size());
            assertEquals("World", removedElement);
        }

        @Test
        @DisplayName("Retrieve and remove the element of the list by index")
        public void removeElementByIndex() {
            String removedElement = list.remove(1);

            assertEquals(1, list.size());
            assertEquals("World", removedElement);
        }
    }

    @Nested
    @DisplayName("Empty list")
    public class EmptyList {
        private final CustomLinkedList<String> emptyList = new CustomLinkedList<>();

        @Test
        @DisplayName("Throw exception when getting element from empty list")
        void throwExceptionWhenGettingElement() {
            assertThrows(NoSuchElementException.class, emptyList::getFirst);
        }

        @Test
        @DisplayName("Throw exception when removing element from empty list")
        void throwExceptionWhenRemovingElement() {
            assertThrows(NoSuchElementException.class, emptyList::removeFirst);
        }

        @Test
        @DisplayName("Throw exception when adding element by invalid index")
        void throwExceptionWhenAddingElementByInvalidIndex() {
            assertThrows(IndexOutOfBoundsException.class, () -> emptyList.add(10, "Index"));
        }

        @Test
        @DisplayName("Throw exception when accessing element by invalid index")
        void throwExceptionWhenAccessingElementByInvalidIndex() {
            assertThrows(IndexOutOfBoundsException.class, () -> emptyList.get(10));
        }

        @Test
        @DisplayName("Throw exception when removing element by invalid index")
        void throwExceptionWhenRemovingElementByInvalidIndex() {
            assertThrows(IndexOutOfBoundsException.class, () -> emptyList.remove(10));
        }

    }

}
