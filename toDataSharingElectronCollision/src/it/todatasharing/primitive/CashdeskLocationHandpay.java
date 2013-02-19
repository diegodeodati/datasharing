package it.todatasharing.primitive;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

public class CashdeskLocationHandpay {

	private long session_id;
	private String aams_location_id;
	private String aams_code_id;
	private String machine_gs_id;
	private long handpay_value;
	private Timestamp handpay_date;
	private String club_id;

	public CashdeskLocationHandpay() {
		session_id = -1;
		aams_location_id = "";
		aams_code_id = "";
		machine_gs_id = "";
		handpay_value = 0;
		handpay_date = new Timestamp(0);
		club_id = "";
	}

	public CashdeskLocationHandpay(long session_id, String aams_location_id,
			String aams_code_id, String machine_gs_id, long handpay_value,
			Timestamp handpay_date, String club_id) {
		super();
		this.session_id = session_id;
		this.aams_location_id = aams_location_id;
		this.aams_code_id = aams_code_id;
		this.machine_gs_id = machine_gs_id;
		this.handpay_value = handpay_value;
		this.handpay_date = handpay_date;
		this.club_id = club_id;
	}

	public long getSession_id() {
		return session_id;
	}

	public void setSession_id(long session_id) {
		this.session_id = session_id;
	}

	public String getAams_location_id() {
		return aams_location_id;
	}

	public void setAams_location_id(String aams_location_id) {
		this.aams_location_id = aams_location_id;
	}

	public String getAams_code_id() {
		return aams_code_id;
	}

	public void setAams_code_id(String aams_code_id) {
		this.aams_code_id = aams_code_id;
	}

	public String getMachine_gs_id() {
		return machine_gs_id;
	}

	public void setMachine_gs_id(String machine_gs_id) {
		this.machine_gs_id = machine_gs_id;
	}

	public long getHandpay_value() {
		return handpay_value;
	}

	public void setHandpay_value(long handpay_value) {
		this.handpay_value = handpay_value;
	}

	public Timestamp getHandpay_date() {
		return handpay_date;
	}

	public void setHandpay_date(Timestamp handpay_date) {
		this.handpay_date = handpay_date;
	}

	/**
	 * @return the club_id
	 */
	public String getClub_id() {
		return club_id;
	}

	/**
	 * @param club_id
	 *            the club_id to set
	 */
	public void setClub_id(String club_id) {
		this.club_id = club_id;
	}

	public void sum(CashdeskLocationHandpay cdlhAdd) {

		this.handpay_value = this.getHandpay_value()
				+ cdlhAdd.getHandpay_value();
		this.club_id = cdlhAdd.club_id;

	}

	public static CashdeskLocationHandpay subtract(
			CashdeskLocationHandpay cPrev, CashdeskLocationHandpay cAct) {
		CashdeskLocationHandpay dCashdeskLocationHandpay = new CashdeskLocationHandpay();

		dCashdeskLocationHandpay.session_id = cAct.session_id;
		dCashdeskLocationHandpay.aams_location_id = cAct.aams_location_id;
		dCashdeskLocationHandpay.aams_code_id = cAct.aams_code_id;
		dCashdeskLocationHandpay.machine_gs_id = cAct.machine_gs_id;
		dCashdeskLocationHandpay.handpay_value = cAct.handpay_value
				- cPrev.handpay_value;
		dCashdeskLocationHandpay.handpay_date = cAct.handpay_date;
		dCashdeskLocationHandpay.club_id = cAct.club_id;

		return dCashdeskLocationHandpay;

	}

	public static HashMap<String, CashdeskLocationHandpay> delta(
			HashMap<String, CashdeskLocationHandpay> hPrev,
			HashMap<String, CashdeskLocationHandpay> hAct) {
		HashMap<String, CashdeskLocationHandpay> map = new HashMap<String, CashdeskLocationHandpay>();

		ArrayList<CashdeskLocationHandpay> list = new ArrayList<CashdeskLocationHandpay>(
				hAct.values());

		for (CashdeskLocationHandpay cslhAct : list) {
			if (hPrev.containsKey(cslhAct.getKey())) {

				CashdeskLocationHandpay cslhPrev = hPrev.get(cslhAct.getKey());

				CashdeskLocationHandpay cInsert = subtract(cslhPrev, cslhAct);

				map.put(cInsert.getKey(), cInsert);

			} else {
				CashdeskLocationHandpay cInsert = subtract(
						new CashdeskLocationHandpay(), cslhAct);
				map.put(cInsert.getKey(), cInsert);

			}
		}

		return map;
	}

	public String getKey() {
		return this.getAams_location_id() + "-"
				+ this.getAams_code_id();
	}

	@Override
	public String toString() {
		return "CashdeskLocationHandpay [session_id=" + session_id
				+ ", aams_location_id=" + aams_location_id + ", aams_code_id="
				+ aams_code_id + ", machine_gs_id=" + machine_gs_id
				+ ", handpay_value=" + handpay_value + ", handpay_date="
				+ handpay_date + "]";
	}

	public CashdeskLocationHandpay clone() {
		CashdeskLocationHandpay cdlhClone = new CashdeskLocationHandpay();

		cdlhClone.session_id = this.session_id;
		cdlhClone.aams_location_id = this.aams_location_id;
		cdlhClone.aams_code_id = this.aams_code_id;
		cdlhClone.machine_gs_id = this.machine_gs_id;
		cdlhClone.handpay_value = this.handpay_value;
		cdlhClone.handpay_date = this.handpay_date;
		cdlhClone.club_id = this.club_id;

		return cdlhClone;

	}

	public HashMap<String, CashdeskLocationHandpay> updateHashMap(
			HashMap<String, CashdeskLocationHandpay> hash) {

		if (!hash.containsKey(this.getKey())) {

			if (this.getHandpay_value() > 0) {
				CashdeskLocationHandpay mHash = new CashdeskLocationHandpay();

				mHash = this.clone();

				hash.put(mHash.getKey(), mHash);
			}

		} else {
			CashdeskLocationHandpay mgIn = hash.get(this.getKey());

			if (this.club_id != mgIn.club_id)
				mgIn.sum(this);
		}

		return hash;

	}

	public boolean isConsiderable() {
		return this.handpay_value != 0;
	}

}
