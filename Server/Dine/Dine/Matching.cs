using System;
using System.Collections.Concurrent;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Dine
{
    public class Matching
    {
        private static Dictionary<string, int> two_nine_ten = new Dictionary<string, int>();
        private static Dictionary<string, int> two_cowell_stevenson = new Dictionary<string, int>();
        private static Dictionary<string, int> two_crown_merrill = new Dictionary<string, int>();
        private static Dictionary<string, int> two_porter_kresge = new Dictionary<string, int>();
        private static Dictionary<string, int> two_rc_oakes = new Dictionary<string, int>();

        private static Dictionary<string, int> three_nine_ten = new Dictionary<string, int>();
        private static Dictionary<string, int> three_cowell_stevenson = new Dictionary<string, int>();
        private static Dictionary<string, int> three_crown_merrill = new Dictionary<string, int>();
        private static Dictionary<string, int> three_porter_kresge = new Dictionary<string, int>();
        private static Dictionary<string, int> three_rc_oakes = new Dictionary<string, int>();

        private static Dictionary<string, int> four_nine_ten = new Dictionary<string, int>();
        private static Dictionary<string, int> four_cowell_stevenson = new Dictionary<string, int>();
        private static Dictionary<string, int> four_crown_merrill = new Dictionary<string, int>();
        private static Dictionary<string, int> four_porter_kresge = new Dictionary<string, int>();
        private static Dictionary<string, int> four_rc_oakes = new Dictionary<string, int>();

        public static List<string> tryMatch(MatchQuery matchQuery)
        {
            List<string> partners = new List<string>();
            string college = "";
            int state = -1;
            bool found = false;
            foreach (int groupSize in matchQuery.getGroupSizes())
            {
                foreach (KeyValuePair<string, int> q in matchQuery.getPreferences())
                {
                    college = q.Key;
                    state = q.Value;
                    if (state == 0)
                        continue;
                    switch (college)
                    {
                        case "nine/ten":
                            {
                                switch (groupSize)
                                {
                                    case 2: { partners.AddRange(handle(ref two_nine_ten, matchQuery.getUserId(), groupSize)); break; }
                                    case 3: { partners.AddRange(handle(ref three_nine_ten, matchQuery.getUserId(), groupSize)); break; }
                                    case 4: { partners.AddRange(handle(ref four_nine_ten, matchQuery.getUserId(), groupSize)); break; }
                                }
                                break;
                            }
                        case "cowell/stevenson":
                            {
                                switch (groupSize)
                                {
                                    case 2: { partners.AddRange(handle(ref two_cowell_stevenson, matchQuery.getUserId(), groupSize)); break; }
                                    case 3: { partners.AddRange(handle(ref three_cowell_stevenson, matchQuery.getUserId(), groupSize)); break; }
                                    case 4: { partners.AddRange(handle(ref four_cowell_stevenson, matchQuery.getUserId(), groupSize)); break; }
                                }
                                break;
                            }
                        case "crown/merill":
                            {
                                switch (groupSize)
                                {
                                    case 2: { partners.AddRange(handle(ref two_crown_merrill, matchQuery.getUserId(), groupSize)); break; }
                                    case 3: { partners.AddRange(handle(ref three_crown_merrill, matchQuery.getUserId(), groupSize)); break; }
                                    case 4: { partners.AddRange(handle(ref four_crown_merrill, matchQuery.getUserId(), groupSize)); break; }
                                }
                                break;
                            }
                        case "porter/kresge":
                            {
                                switch (groupSize)
                                {
                                    case 2: { partners.AddRange(handle(ref two_porter_kresge, matchQuery.getUserId(), groupSize)); break; }
                                    case 3: { partners.AddRange(handle(ref three_porter_kresge, matchQuery.getUserId(), groupSize)); break; }
                                    case 4: { partners.AddRange(handle(ref four_porter_kresge, matchQuery.getUserId(), groupSize)); break; }
                                }
                                break;
                            }
                        case "rc/oakes":
                            {
                                switch (groupSize)
                                {
                                    case 2: { partners.AddRange(handle(ref two_rc_oakes, matchQuery.getUserId(), groupSize)); break; }
                                    case 3: { partners.AddRange(handle(ref three_rc_oakes, matchQuery.getUserId(), groupSize)); break; }
                                    case 4: { partners.AddRange(handle(ref four_rc_oakes, matchQuery.getUserId(), groupSize)); break; }
                                }
                                break;
                            }
                    }
                    if (partners.Count > 0)
                    {
                        remove(matchQuery.getUserId());
                        foreach (string s in partners)
                            remove(s);
                        found = true;
                    }
                    if (found)
                        break;
                }
                if (found)
                    break;
            }
            printQ();
            return partners;
        }

        private static List<string> handle(ref Dictionary<string, int> q, string id, int groupSize)
        {
            List<string> partners = new List<string>();
            if (q.Count < groupSize - 1)
                q.Add(id, 1);
            else if (q.Count >= groupSize - 1)
            {
                for (int index = 0; index < groupSize - 1; index++)
                    partners.Add(q.ElementAt(index).Key);
            }
            return partners;
        }

        public static bool remove(string id)
        {
            two_nine_ten.Remove(id);
            two_cowell_stevenson.Remove(id);
            two_crown_merrill.Remove(id);
            two_porter_kresge.Remove(id);
            two_rc_oakes.Remove(id);

            three_nine_ten.Remove(id);
            three_cowell_stevenson.Remove(id);
            three_crown_merrill.Remove(id);
            three_porter_kresge.Remove(id);
            three_rc_oakes.Remove(id);

            four_nine_ten.Remove(id);
            four_cowell_stevenson.Remove(id);
            four_crown_merrill.Remove(id);
            four_porter_kresge.Remove(id);
            four_rc_oakes.Remove(id);

            printQ();
            return true;
        }

        private static void printQ()
        {
            Console.WriteLine("=================Queues=================" + Environment.NewLine);
            Console.WriteLine("2-Nine/Ten - " + two_nine_ten.Count() + " | 3-Nine/Ten - " + three_nine_ten.Count() + " | 4-Nine/Ten - " + four_nine_ten.Count());
            Console.WriteLine("2-Cowell/Stevenson - " + two_cowell_stevenson.Count() + " | 3-Cowell/Stevenson - " + three_cowell_stevenson.Count() + " | 4-Cowell/Stevenson - " + four_cowell_stevenson.Count());
            Console.WriteLine("2-Crown/Merrill - " + two_crown_merrill.Count() + " | 3-Crown/Merrill - " + three_crown_merrill.Count() + " | 4-Crown/Merrill - " + four_crown_merrill.Count());
            Console.WriteLine("2-Porter/Kresge - " + two_porter_kresge.Count() + " | 3-Porter/Kresge - " + three_porter_kresge.Count() + " | 4-Porter/Kresge - " + four_porter_kresge.Count());
            Console.WriteLine("2-RC/Oakes - " + two_rc_oakes.Count() + " | 3-RC/Oakes - " + three_rc_oakes.Count() + " | 4-RC/Oakes - " + four_rc_oakes.Count());
            Console.WriteLine("");
            Console.WriteLine("========================================");
        }
    }
}
