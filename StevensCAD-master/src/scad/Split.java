package scad;

import java.util.List;

public class Split <T>{

        private final T inside, outside;

        public T inside() {
            return inside;
        }

        public T outside() {
            return outside;
        }

        public boolean hasOutside() {
            return outside != null;
        }

        public boolean hasInside() {
            return inside != null;
        }

        public boolean isCleaved() {
            return outside != null && inside != null;
        }

        public Split(T inside, T outside) {
            this.inside = inside;
            this.outside = outside;
        }

        public boolean onSurface() {//coincident edges
            return inside != null && outside != null && inside.equals(outside);
        }
        
    }