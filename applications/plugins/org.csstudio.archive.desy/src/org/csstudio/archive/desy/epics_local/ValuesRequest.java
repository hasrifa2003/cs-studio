package org.csstudio.archive.desy.epics_local;

import org.csstudio.archive.ArchiveValues;
import org.csstudio.archive.desy.archiveRecord.ArchiveRecord;
import org.csstudio.platform.util.ITimestamp;
import org.csstudio.platform.util.TimestampFactory;
import org.csstudio.value.DoubleValue;
import org.csstudio.value.MetaData;
import org.csstudio.value.NumericMetaData;
import org.csstudio.value.Severity;
import org.csstudio.value.Value;
/** Handles the "archiver.values" request and its results.
 *  @author Albert Kagarmanov
 */
public class ValuesRequest implements ClientRequest
{
	private ArchiveServer server;
	private int key;
	private String channels[];
	private ITimestamp start, end;
	private int how;
    private Object parms[];
	
	// Possible 'type's for the values.
	private static final int TYPE_STRING = 0;
	private static final int TYPE_ENUM = 1;
	private static final int TYPE_INT = 2;
	private static final int TYPE_DOUBLE = 3;
	
	// The result of the query
	private ArchiveValues archived_samples[];
	
	/** Constructor for new value request.
	 *  <p>
	 *  Details regarding the meaning of 'count' for the different 'how'
	 *  values are in the XML-RPC description in the ChannelArchiver manual.
	 *  
	 *  @param key Archive key
	 *  @param channels Vector of channel names
	 *  @param start Start time for retrieval
	 *  @param end  End time for retrieval
	 *  @param count How many values to get
	 *  @param how How to retrieve
	 */
	public ValuesRequest(ArchiveServer server,
			int key, String channels[],
			ITimestamp start, ITimestamp end,
            int how, Object parms[])
	{
		this.server = server;
		this.key = key;
		this.channels = channels;
		this.start = start;
		this.end = end;
		this.how = how;
        this.parms = parms;
	}

	/** @see org.csstudio.archive.channelarchiver.ClientRequest#read() */
	public int read()  throws Exception
	{
		int error = 0;    
        int num_returned_channels = this.channels.length;
        archived_samples = new ArchiveValues[num_returned_channels];   
        for (int COUNT=0;COUNT< this.channels.length;COUNT++) 
        {
        	ArchiveRecord ar = new ArchiveRecord(this.channels[COUNT]);
    		int dim = ar.getDimension();
    		if (dim<=0) {
    			System.out.println("bad Dim");
    			throw new Exception("Expected 'Integer count' for " + this.channels[COUNT]);
    		}
    		ar.getAllFromCA();
			int type = TYPE_DOUBLE; //  TODO answerClass.getType(); AR return only Double
			int count = 1; // do not use WF answerClass.getCount();
			int num_samples=dim;
			Value samples[]= new Value[num_samples];
			MetaData meta = new NumericMetaData(
					100,0, //DisplayHigh(),DisplayLow(),
					100,0, //High,LowAlarm(),
					100,0, //High,LowWarn(),
					2, " "//Precision(),Egu()
					);	
			for (int si=0; si<num_samples; si++) {
				long secs = ar.getTime()[si];
				long nano = ar.getNsec()[si];
				ITimestamp time = TimestampFactory.createTimestamp(secs, nano);
				System.out.println("TIME=="+time.toString());
				
				int stat = 0; // no stat for archive record
				int sevr = (int) ar.getSevr()[si];
				Severity sevClass= new SeverityImpl("",true,true);
				double values[] = new double[count]; // count=1
			    for (int vi=0; vi<count; ++vi) values[vi] = ar.getVal()[si];
				samples[si] = new DoubleValue(time, sevClass,"", meta, values);
			}
			archived_samples[COUNT] =
				new ArchiveValues(server, this.channels[COUNT], samples );		
        } // end of foreach COUNT=0 ... channels.length 
        
        return error;
	}

	/** @return Returns one <code>ArchivedValues</code> per channel. */
	public ArchiveValues[] getArchivedSamples()
	{
		return archived_samples;
	}	
}
