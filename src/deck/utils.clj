(ns deck.utils)

(defn get-comparator-dropdown
  "This is to generate a comparator Drop Down List"
  []
  (str "<select name='comparator'>"
       "<option value=':long'>long</option>"
       "<option value=':byte'>byte</option>"
       "<option value='utf8'>UTF8</option>"
       "</select>"))


(defn get-comlumnfamily-type-dropdown
  "This is to generate a column family type Drop Down List"
  []
  (str "<select name='columnfamilytype'>"
       "<option value='standard'>Standard</option>"
       "<option value='super'>Super</option>"
       "</select>"))