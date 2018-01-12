package sayPotato;

import java.util.ArrayList;

public class MelFilterFactor {

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

    public static void main(String[] args) {
        MelFilterFactor xD = new MelFilterFactor();
        ArrayList<TriangleMelFilter> filters = xD.setOfFiltersMel(TriangleMelFilter.class, 10,5,10);

        for (MelFilter filter:filters) {
            System.out.println(filter.getStartFreqMel() + " : " + filter.getEndFreqMel());
            System.out.println(" ");
        }

    }

}
