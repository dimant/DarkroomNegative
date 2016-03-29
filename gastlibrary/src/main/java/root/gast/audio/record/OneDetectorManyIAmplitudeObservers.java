/*
 * Copyright 2012 Greg Milette and Adam Stroud
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * 		http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package root.gast.audio.record;

import java.util.List;


/**
 * allow many listeners to receive the data, but only one to act on it
 * @author Greg Milette &#60;<a href="mailto:gregorym@gmail.com">gregorym@gmail.com</a>&#62;
 */
public class OneDetectorManyIAmplitudeObservers implements IAmplitudeClipListener
{
    private IAmplitudeClipListener detector;
    
    private List<IAmplitudeClipListener> observers;

    public OneDetectorManyIAmplitudeObservers(IAmplitudeClipListener detector, List<IAmplitudeClipListener> observers)
    {
        this.detector = detector;
        this.observers = observers;
    }
    
    @Override
    public boolean heard(int maxAmplitude)
    {
        for (IAmplitudeClipListener observer : observers)
        {
            observer.heard(maxAmplitude);
        }
        
        return detector.heard(maxAmplitude);
    }
}
