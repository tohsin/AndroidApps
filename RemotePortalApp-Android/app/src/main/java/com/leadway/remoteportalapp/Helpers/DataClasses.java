package com.leadway.remoteportalapp.Helpers;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class DataClasses {
    public static class Branch{
        public String address;
        public String branchName;
        public String branch_id;


        public Branch(String address, String branchName, String branch_id) {
            this.address = address;
            this.branchName = branchName;
            this.branch_id = branch_id;
        }

        @NonNull
        @Override
        public String toString() {
            return branchName;
        }
    }

    public static class BriefRequestDetail {
        public String branchName;
        public String department;
        public String fullName;
        public int request_id;

        public BriefRequestDetail(String branchName, String department, String fullName, int request_id) {
            this.branchName = branchName;
            this.department = department;
            this.fullName = fullName;
            this.request_id = request_id;
        }
    }

    public static class CanteenDetails{
        public int cost;
        public String createdDate;
        public String emailAddress;
        public String fullName;
        public String response;
        public int scan_id;
        public String staffNo;
        public String terminal;
        public String vendorName;
        public int vendor_id;

        public CanteenDetails(int cost, String createdDate, String emailAddress, String fullName, String response, int scan_id, String staffNo, String terminal, String vendorName, int vendor_id) {
            this.cost = cost;
            this.createdDate = createdDate;
            this.emailAddress = emailAddress;
            this.fullName = fullName;
            this.response = response;
            this.scan_id = scan_id;
            this.staffNo = staffNo;
            this.terminal = terminal;
            this.vendorName = vendorName;
            this.vendor_id = vendor_id;
        }
    }

    public static class BriefSessionDetails {
        public String branchName;
        public String dayNumber;
        public int session_id;
        public String timeRange;
        public String workDate;

        public BriefSessionDetails(String branchName, String dayNumber, int session_id, String timeRange, String workDate) {
            this.branchName = branchName;
            this.dayNumber = dayNumber;
            this.session_id = session_id;
            this.timeRange = timeRange;
            this.workDate = workDate;
        }
    }

    public static class IndepthSessionDetails implements Parcelable {
        public Boolean checkedIn;
        public String checkedInDate;
        public String checkedInTime;
        public String createdDate;
        public String durationFromNow;
        public String endTime;
        public int session_id;
        public String startTime;
        public String workDate;

        public IndepthSessionDetails(Boolean checkedIn, String checkedInDate, String checkedInTime, String createdDate, String durationFromNow, String endTime, int session_id, String startTime, String workDate) {
            this.checkedIn = checkedIn;
            this.checkedInDate = checkedInDate;
            this.checkedInTime = checkedInTime;
            this.createdDate = createdDate;
            this.durationFromNow = durationFromNow;
            this.endTime = endTime;
            this.session_id = session_id;
            this.startTime = startTime;
            this.workDate = workDate;
        }

        protected IndepthSessionDetails(Parcel in) {
            byte tmpCheckedIn = in.readByte();
            checkedIn = tmpCheckedIn == 0 ? null : tmpCheckedIn == 1;
            checkedInDate = in.readString();
            checkedInTime = in.readString();
            createdDate = in.readString();
            durationFromNow = in.readString();
            endTime = in.readString();
            session_id = in.readInt();
            startTime = in.readString();
            workDate = in.readString();
        }

        public static final Creator<IndepthSessionDetails> CREATOR = new Creator<IndepthSessionDetails>() {
            @Override
            public IndepthSessionDetails createFromParcel(Parcel in) {
                return new IndepthSessionDetails(in);
            }

            @Override
            public IndepthSessionDetails[] newArray(int size) {
                return new IndepthSessionDetails[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeByte((byte) (checkedIn == null ? 0 : checkedIn ? 1 : 2));
            dest.writeString(checkedInDate);
            dest.writeString(checkedInTime);
            dest.writeString(createdDate);
            dest.writeString(durationFromNow);
            dest.writeString(endTime);
            dest.writeInt(session_id);
            dest.writeString(startTime);
            dest.writeString(workDate);
        }
    }

    public static class WorkUsers{
        public String branchName;
        public String dateFrom;
        public String dateTo;
        public String fullname;
        public String staffNo;
        public String username;

        public WorkUsers(String branchName, String dateFrom, String dateTo, String fullname, String staffNo, String username) {
            this.branchName = branchName;
            this.dateFrom = dateFrom;
            this.dateTo = dateTo;
            this.fullname = fullname;
            this.staffNo = staffNo;
            this.username = username;
        }
    }

    public static class SurveyQnA{
        public String questionName;
        public int questionID;
        public ArrayList<String> answers;

        public SurveyQnA(String questionName, int questionID, ArrayList<String> answers) {
            this.questionName = questionName;
            this.questionID = questionID;
            this.answers = answers;
        }
    }
}
