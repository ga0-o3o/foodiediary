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
	private static final MathTransform FROM_UTMK;
	
	static {
		try {
			// 축 순서 명시적 설정
			WGS84 = CRS.decode("EPSG:4326", true); // 경도(longitude), 위도(latitude)
			UTMK = CRS.decode("EPSG:5174");  // X(Easting), Y(Northing)
			
			// 변환 파이프라인 생성
			TO_UTMK = CRS.findMathTransform(WGS84, UTMK, true);
			FROM_UTMK = CRS.findMathTransform(UTMK, WGS84, true);
		} catch (FactoryException e) {
			throw new RuntimeException("CRS 초기화 실패", e);
		}
	}
	
	// WGS84 → UTM-K 변환 (정확한 축 순서 처리)
	public static double[] toUtmk(double latitude, double longitude) {
		try {
			double[] src = new double[]{longitude, latitude};
			double[] dest = new double[2];
			TO_UTMK.transform(src, 0, dest, 0, 1);
			return dest; // [x, y]
		} catch (TransformException e) {
			throw new RuntimeException("좌표 변환 실패", e);
		}
	}
	
	// UTM-K → WGS84 역변환 추가
	public static double[] toWgs84(double x, double y) {
		try {
			double[] src = new double[]{x, y};
			double[] dest = new double[2];
			FROM_UTMK.transform(src, 0, dest, 0, 1);
			return new double[]{dest[1], dest[0]}; // [latitude, longitude]
		} catch (TransformException e) {
			throw new RuntimeException("좌표 역변환 실패", e);
		}
	}
	
	// 테스트 코드 개선
	public static void main(String[] args) {
		// 서울시청 좌표 변환 테스트
		double[] utmk = toUtmk(37.5665, 126.9780);
		System.out.printf("UTM-K 변환: x=%.2f, y=%.2f%n", utmk[0], utmk[1]);
		
		// 역변환 검증
		double[] wgs84 = toWgs84(utmk[0], utmk[1]);
		System.out.printf("WGS84 복원: lat=%.6f, lon=%.6f", wgs84[0], wgs84[1]);
	}
}