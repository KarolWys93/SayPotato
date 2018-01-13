package sayPotato;

import java.util.ArrayList;

/**
 * Mel-scale filter factor creates a set of filters.
 * @see MelFilter
 */
public class MelFilterFactor {

    /**
     * Creates mel-scale filters set. This generic method creates filterNum filters with fixed filter width
     * (in mel scale) and with overlay (in mel scale).
     * Overlay means, if the filters have 100 mel width and 50 mel overlay, first filter has range
     * between 0 and 100 mel, second has range 50 - 150, third has range 100 - 200 etc.
     * @param tClass Class of created filters.
     * @param filterWidth width of filter band in mel scale
     * @param overlay filter overlay in mel scale
     * @param filterNum dilters number
     * @param <T> Type of created filters
     * @return ArrayList of filters
     */
    public <T extends MelFilter> ArrayList<T> setOfFiltersMel(Class<T> tClass, double filterWidth, double overlay, int filterNum){

        if(overlay >= filterWidth){
            throw new IllegalArgumentException("Overlay must be smaller than filter width.");
        }

        ArrayList <T> filtersArray = new ArrayList<>(filterNum);
        double startMel;
        double endMel;

        for (int i = 0; i < filterNum; i++) {
            try {
                startMel = i * (filterWidth-overlay);
                endMel = startMel + filterWidth;

                T filter = tClass.newInstance();
                filter.setBandMel(startMel, endMel);
                filtersArray.add(filter);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        return filtersArray;
    }
}
