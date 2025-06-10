package foodiediary.restaurant;

import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.referencing.CRS;

public class CoordinateConverter {
	private static final CoordinateReferenceSystem WGS84;
	private static final CoordinateReferenceSystem UTMK;
	private static final MathTransform TO_UTMK;
	
	static {
		try {
			WGS84 = CRS.decode("EPSG:4326", true); // 경도, 위도
			UTMK = CRS.decode("EPSG:5174");
			TO_UTMK = CRS.findMathTransform(WGS84, UTMK, true);
		} catch (FactoryException e) {
			throw new RuntimeException("CRS 초기화 실패", e);
		}
	}
	
	public static double[] toUtmk(double latitude, double longitude) {
		try {
			double[] src = new double[]{longitude, latitude};
			double[] dest = new double[2];
			TO_UTMK.transform(src, 0, dest, 0, 1);
			return new double[]{dest[1], dest[0]}; // 축 순서 변경
		} catch (TransformException e) {
			throw new RuntimeException("좌표 변환 실패", e);
		}
	}
	
	public static void main(String[] args) {
		double[] result = toUtmk(37.5665, 126.9780);
		System.out.printf("변환 결과: x=%.2f, y=%.2f%n", result[0], result[1]);
	}
}